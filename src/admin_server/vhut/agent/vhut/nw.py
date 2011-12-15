# -*- coding: utf-8 -*-
'''
Copyright 2011 NTT Software Corporation.
All Rights Reserved.

@author NTT Software Corporation.
@version 1.0.0

$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
$Revision: 949 $
$Author: NTT Software Corporation. $
'''

import cPickle
import logging
from ConfigParser import ConfigParser
import os
from os.path import *
from threading import RLock
import math
import re
import shutil
import subprocess
import time
from xmlrpclib import Fault

class NwException(Exception):
    """ネットワークハンドラの例外
    """
    def __init__(self, cmd, error, step=0):
        if isinstance(cmd, str):
            self.cmd =cmd
        else:
            self.cmd = ' '.join(self.cmd)
        self.step = step
        self.error = error

    def __str__(self):
        return "In  '%s'(%s):  message: %s" % (self.cmd, self.step, self.error)


class NwConfig(object):
    """仮想化開発環境ネットワーク制御共有設定クラス
含まれるデータ属性
 initialized
  数値: 初期化フラグ。初期化未の状態では0。初期化済の状態では1
 log
  文字列: ログファイル名
 loglevel
  数値: ログ出力レベル
 public_if
  文字列: サーバのパブリック側IF
 public_netmask
  文字列: パブリック側に設定するIPのサブネットマスク
 private_if
  文字列: サーバのプライベート側IF
 private_network
  文字列: サーバ自身のプライベート側のネットワークアドレス
 private_netmask
  文字列: サーバ自身のプライベート側のサブネットマスク
 addresses
  辞書: インスタンスのIPアドレスと、MACアドレスの対応
   例: {'192.168.0.1': '00:AA:BB:CC:DD:00', '192.168.1.1': '00:AA:BB:CC:DD:01'}
 networks
  辞書: VLAN IDごとのネットワーク情報
   例: {10: {'nameserver': '192.168.0.1', 'dhcp': '192.168.0.2', 'gateway': '192.168.0.254', 'broadcast': '192.168.0.255', 'netmask': '255.255.255.0', 'network': '192.168.0.0', user: "test01-deault"},
        11: {'nameserver': '192.168.0.1', 'dhcp': '192.168.1.2', 'netmask': '255.255.255.0', 'broadcast': '192.168.1.255', 'gateway': '192.168.1.254', 'networks': '192.168.1.0', user: "test02-deault"}}
 publicips
  辞書: NAT変換されるパブリックIPと、プライベートIPの対応
    例: {'172.30.112.168': '192.168.1.1'}
 netfilter
   文字列: "iptables-save"の出力結果

その他:
 永続化
  NwConfigは、NwHandlerの、self._configとして参照される。
  クラスは永続化されてファイルとして存在するが、このファイル名はself._config_fileとして定義されている。

 排他処理
  永続化ファイルの読み込み時には、ロックファイルが作成され、他プロセスからの利用を排他する。
    """
    initialized = 0
    role = 'manager'
    public_if = 'eth0'
    public_netmask = '20'
    private_if = 'eth1'
    private_network = '192.168.0.0'
    private_netmask = '24'
    addresses = {}
    networks =  {}
    netfilter = ""
    publicips = {}

    def to_dict(self):
        result = dict()
        result["initialized"] = self.initialized
        result["public_if"] = self.public_if
        result["public_netmask"] = self.public_netmask
        result["private_if"] = self.private_if
        result["private_network"] = self.private_network
        result["private_netmask"] = self.private_netmask
        result["netfilter"] = self.netfilter
        result["addresses"] = self.addresses
        result["networks"] = self.networks
        result["publicips"] = self.publicips
        return result

    def __getstate__(self):
        return self.to_dict()

    def __setstate__(self, dict):
        self.initialized = dict['initialized']
        self.public_if = dict['public_if']
        self.public_netmask = dict['public_netmask']
        self.private_if = dict['private_if']
        self.private_network = dict['private_network']
        self.private_netmask = dict['private_netmask']
        self.addresses = dict['addresses']
        self.networks =  dict['networks']
        self.netfilter = dict['netfilter']
        self.publicips = dict['publicips']
        self.networks = dict['networks']


class LockedPickleException(Exception):
    """ロック処理を追加したShelve管理オブジェクトの例外
    """
    def __init__(self, error):
        self.error = error

    def __str__(self):
        return "message: %s" % (self.error)

class LockedPickle(object):
    """ロック処理を追加したcPickle管理オブジェクト
    """
    def __init__(self, path, roll_back_func=None):
        self._logger = logging.getLogger('vhuta.nw.LockedPickle')
        self._path = path
        self.roll_back_func = roll_back_func
        self._lock = RLock() #ロックの生成
        # Shelveがパージングできるか前もって試験
        self._lock.acquire()
        try:
            if exists(self._path):
                try:
                    f = open(self._path, 'rb')
                    self._lp = cPickle.load(f)
                    f.close()
                    self._logger.debug("__init__: config is restored from dumped file.")
                    self._logger.debug(self._lp.to_dict())
                except:
                    msg = "cPickle open error : caused by opennig or parsing"
                    self._logger.error(msg)
                    raise LockedPickleException(msg)
            else:
                self._lp = NwConfig()
                f = open(self._path, 'wb')
                cPickle.dump(self._lp, f, -1)
                f.close()
                self._logger.debug("__init__: config is renewed.")
        finally:
            self._lp = None
            self._lock.release()

    def __enter__(self):
        self._lock.acquire() #ロックの取得
        try:
            f = open(self._path, 'rb')
            self._lp = cPickle.load(f)
            f.close()
            self._logger.debug(self._lp.to_dict())
        except:
            self._lock.release() #ロックの解放
            msg = "cPickle open error : caused by opennig or parsing"
            self._logger.error(msg)
            raise LockedPickleException(msg)
        return self._lp

    def __exit__(self, exc_type=None, exc_value=None, traceback=None):
        rc = False
        try:
            if exc_type is None:
                try:
                    f = open(self._path, 'wb')
                    cPickle.dump(self._lp, f, -1)
                    f.close()
                    self._logger.debug("__exit__: config is upadated.")
                    self._logger.debug(self._lp.to_dict())
                except:
                    msg = "cPickle dump error : caused by parsing"
                    self._logger.error(msg)
                    raise LockedPickleException(msg)
                else:
                    rc = True
            elif exc_type == 1:
                rc = True
            elif exc_type and self.roll_back_func is not None:
                try:
                    f = open(self._path, 'rb')
                    old_db = cPickle.load(f)
                    f.close()
                    try:
                        old_db = self.roll_back_func(old_db, self._lp, exc_type, exc_value)
                    except:
                        old_db.initialized = 0
                        msg = "recover faild"
                        self._logger.error(msg)
                        raise LockedPickleException(msg)
                    else:
                        old_db.initialized = 1
                finally:
                    f = open(self._path, 'wb')
                    cPickle.dump(old_db, f, -1)
                    f.close()
                    self._logger.debug("__exit__: config is rollebacked.")

        finally:
            self._lock.release() #ロックの解放
            return rc


class NwHandler:
    """ネットワーク制御のハンドラ
    """
    _iptables = '/sbin/iptables'
    _iptables_save = '/sbin/iptables-save'
    _iptables_restore = '/sbin/iptables-restore'
    _vconfig = '/sbin/vconfig'
    _brctl = '/usr/sbin/brctl'
    _iproute = '/sbin/ip'

    _vconfig_dir = '/proc/net/vlan'
    _vconfig_file = '/proc/net/vlan/config'

    _lp_file = None
    _lp = None

    _dhcpd = '/usr/sbin/dhcpd'
    _dhcp_conf = '/var/tmp/dhcp.conf'
    _dhcp_leases = '/var/tmp/dhcp.leases'
    _dhcpd_pid = '/var/tmp/dhcp.pid'
    _dhcp_trace = '/var/tmp/dhcp.trace'

    _conf = False

    _dbpath = False

    _confkey_nw = "nw"

    __version__ = '0.20100303'

    def __init__(self, config_file, db_file):
        #設定ファイル展開
        default = {'role' : 'manager'
                   ,'public_if' : 'eth0'
                   ,'public_netmask' : '24'
                   ,'private_if' : 'eth1'
                   ,'private_network' : '192.168.0.1'
                   ,'private_netmask' : '24'
                   ,'public_if_preserve' : 'false'}
        self._config = ConfigParser(default)
        self._config.read(config_file)

        self._public_if_preserve = self._config.getboolean(self._confkey_nw, 'public_if_preserve')

        #ロギング設定
        self._logger = logging.getLogger('vhut.nw.NwHandler')
        try:
            self._lp = LockedPickle(db_file, self._roll_back)
            self._logger.debug('__init__: config is successfully prepared.')
        except:
            self._logger.error('__init__', '__init__ failed (dumped config object restore fails).')

    def end(self):
        self._logger.info("end: called")
        rc = False
        try:
            conf = self._lp.__enter__()
            try:
                self._stop_dhcpd()
                self._wipe(conf.private_if, conf.public_if)
            except:
                if not self._lp.__exit__(*sys.exc_info()):
                    msg = "end is failed"
                    self._logger.exception(msg)
                    raise NwException('end', msg)
            else:
                self._lp.__exit__(1)
                rc = True
        finally:
            self._logger.info("end: returns %s" % rc)
            return rc

    def init(self, clear_all=False, nodump=None):
        """初期化
引数:
 1. clear_all Trueの時：dbをクリアする
 2. nodump 1の時：Configファイルのダンプをキャンセルする
効果:
処理概要:
 1. ネットワーク関連の設定を削除し、初期状態(netfilter初期設定)を設定する。
 2. 設定ファイルが更新されている場合、もしくはclear_allがTrueの場合はdbをクリアし未初期化とする。
 3. dbが未初期化でない場合は、状態を復元する。
 4. dbが未初期化の場合は、dbを初期化済みとする。
例:
  nw = NwHandler()
  nw.init()
        """
        self._logger.info("init: called")
        rc = False
        try:
            conf = self._lp.__enter__()
            try:
                if not clear_all:
                    #重要な設定が変更になっていないかチェック
                    if conf.public_if != self._config.get(self._confkey_nw, 'public_if')\
                    or conf.public_netmask != self._config.get(self._confkey_nw, 'public_netmask')\
                    or conf.private_if != self._config.get(self._confkey_nw, 'private_if')\
                    or conf.private_network != self._config.get(self._confkey_nw, 'private_network')\
                    or conf.private_netmask != self._config.get(self._confkey_nw, 'private_netmask'):
                        clear_all = True
                if clear_all:
                    try:
                        #設定を全クリア
                        self._logger.info('init: clear pickled conf')
#                        conf = self._lp.clear()
                        #初期設定
                        conf.public_if = self._config.get(self._confkey_nw, 'public_if')
                        conf.public_netmask = self._config.get(self._confkey_nw, 'public_netmask')
                        conf.private_if = self._config.get(self._confkey_nw, 'private_if')
                        conf.private_network = self._config.get(self._confkey_nw, 'private_network')
                        conf.private_netmask = self._config.get(self._confkey_nw, 'private_netmask')
                        conf.addresses = {}
                        conf.networks =  {}
                        conf.netfilter = ""
                        conf.publicips = {}
                    except:
                        self._logger.error('init: setting configuration is failed.')
                        raise
                #復帰を試みる
                self._wipe(conf.private_if, conf.public_if)
                self._netfilter_initialize(conf.private_network, conf.private_netmask)
                self._revive(conf.public_if, conf.private_if, conf.addresses, conf.networks, conf.netfilter, conf.publicips, conf.public_netmask)
                conf.initialized = 1
            except:
                if not self._lp.__exit__(*sys.exc_info()):
                    msg = "init is failed"
                    self._logger.exception(msg)
                    raise NwException('init', msg)
            else:
                rc = self._lp.__exit__(nodump)
        finally:
            self._logger.info("init: returns %s" % rc)
            return rc

    def clear(self, nodump=None):
        """初期化
引数:
 1. nodump 1の時：Configファイルのダンプをキャンセルする
処理概要:カーネル上の設定(netfilter, VLAN関連)をすべてプレーンな状態にする。dbのデータをすべて削除する。
 1. ネットワーク関連の設定を削除し、初期状態(netfilter初期設定)を設定する。
 2. dbをクリアし未初期化とする。
 3. dbを初期化済みとする
         """
        self._logger.info("clear: called")
        rc = self.init(True, nodump)
        self._logger.info("clear: returns %s" % rc)
        return rc

    def _wipe(self, private_if, public_if):
        """全削除
引数: なし
効果: カーネル上の設定(netfilter, VLAN関連)をすべてプレーンな状態にする。
      共有オブジェクトの内容は変更しないため、wipeを実行後は、reviveメソッドで復旧するか、共有オブジェクトの実体を削除しないと、以降の正常動作は保証されない。
処理概要:
 1. netfilterの各テーブルをすべてフラッシュする
 2. VLANインターフェイスをすべて削除する。
 3. パブリックIFに付与されている自アドレス以外のすべてのアドレスを削除する。
 4. dhcpを停止する。
例:
  nw = NwHandler()
  nw.wipe()
失敗時の対応: 実行中に処理が失敗した場合、共有設定オブジェクトと、マシンの状態は可能な限りメソッド実行前に戻される。
        """
        try:
            self._logger.info("_wipe: called")
            self._put_iptables('filter', ['-F'])
            self._put_iptables('nat', ['-F'])
            self._put_iptables('filter', ['-P', 'FORWARD', 'ACCEPT'])
            self._put_iptables('filter', ['-F', 'FORWARD'])
            self._put_iptables('nat', ['-F', 'POSTROUTING'])
            self._put_iptables('nat',['-F', 'PREROUTING'])
            self._put_iptables('nat',['-F', 'OUTPUT'])
            self._put_iptables('filter', ['-X'])
        except:
            msg = '_wipe: netfilter setting failed.'
            self._logger.error(msg)
            raise NwException('_wipe', msg)
        else:
            try:
                vlan_devs = self._get_vlan_devs(private_if)
                for vlan_dev in vlan_devs:
                    self._link_down(vlan_dev)
                    self._vconfig_del(vlan_dev)
            except:
                msg = '_wipe: vlan removing from private_if failed.'
                self._logger.error(msg)
                raise NwException('_wipe', msg)
            else:
                try:
                    self._ip_flush(public_if, self._public_if_preserve)
                except:
                    msg = '_wipe: all public ip removing from interface failed.'
                    self._logger.error(msg)
                    raise NwException('_wipe', msg)
                else:
                    try:
                        self._stop_dhcpd()
                    except:
                        msg = '_wipe: dhcpd stoppping failed.'
                        self._logger.error(msg)
                        raise NwException('_wipe', msg)
                    else:
                        self._logger.info('_wipe: succeed.')

    def _revive(self, public_if, private_if, addresses, networks, netfilter, publicips, public_netmask):
        """復元
引数: なし
効果: 共有オブジェクトに保存されている設定内容を復元する。共有オブジェクトが存在しなければ何もしない。復元対象は以下の通り
     ・VLAN IFなどネットワークインターフェイス設定
     ・netfilter
     ・DHCPデーモン設定

処理概要:
 1. 共有設定オブジェクトをロードする。
 2. 使用中パブリックアドレスの全てを、パブリックIFに付与
 3. 記録されている全てのVLAN IDのVLANインターフェイスを作成
 4. 記録されている全てのVLAN IDのブリッジインターフェイスを作成
 5. 作成したインターフェイスを活性化
 6. 記録されている全てのVLAN IDのネットワークのゲートウェイアドレスを、各VLANインターフェイスに付与
 7. 記録されているnetfilterの文字列を、"iptables-restore"に渡しnetfilter設定を行う。
 8. 共有設定オブジェクト内の情報から、DHCPデーモン設定ファイルを再作成する。
 9. DHCPデーモンを再起動する。

例:
  nw = NwHandler()
  nw.revive()

失敗時の対応: 実行中に処理が失敗した場合、共有設定オブジェクトと、マシンの状態は可能な限りメソッド実行前に戻される。
        """
        if len(networks) == 0:
            self._logger.info('_revive: called but noting to do.')
        else:
            self._logger.info('_revive: called.')
#        for nw in networks:
        for i in networks.keys():
            nw = networks[i]
            self._logger.debug("_revive: start %s config." % (nw["username"]))
            try:
                self._link_down(private_if + '.' + str(i))
                self._vconfig_del(private_if, i)
                self._ip_del(private_if + '.' + str(i), nw["dhcp"], str(self._mask2bit(nw["netmask"])))
            except:
                self._logger.warning("revive: VLAN: %d (%s) was already removed." % (i, nw["network"]))
            else:
                self._logger.debug("revive: VLAN: %d (%s) was removed." % (i, nw["network"]))
            try:
                self._vconfig_add(private_if, i)
                self._link_up(private_if + '.' + str(i))
                self._ip_add(private_if + '.' + str(i), nw["dhcp"], str(self._mask2bit(nw["netmask"])))
                self._link_up(private_if)
            except:
                msg = 'network interface setting failed.'
                self._logger.error(msg)
                raise NwException('revive', msg)
            else:
                self._logger.debug("revive: VLAN: %d (%s) was revived." % (i, nw["network"]))
        try:
            for j in publicips.keys():
                self._ip_add(public_if, j, public_netmask)
        except:
            msg = 'public ip putting to interface failed.'
            self._logger.error(msg)
            raise NwException('revive', msg)
        else:
            self._logger.debug('revive: public ip putting to interface.')
            try:
                self._do_iptables_restore(netfilter)
            except:
                msg = 'netfilter restore failed'
                self._logger.error(msg)
                raise NwException('revive', msg)
            else:
                self._logger.debug('revive: netfilter restored.')
                try:
                    self._kick_dhcpd(networks, addresses, private_if)
                except:
                    msg = 'dhcpd starting failed.'
                    self._logger.error(msg)
                    raise NwException('revive', msg)
                else:
                    self._logger.debug('revive: dhcpd started.')
                    self._logger.info('revive: revive succeed.')


    def _roll_back(self, old_conf, new_conf=None, exc_type=None, exc_value=None):
        """ロールバックを実行する。
        """
        res_conf = old_conf
        try:
            if exc_type is NwException:
                cmd = exc_value.cmd
                step = exc_value.step
                while step > 0:
                    try:
                        self._logger.debug("_roll_vack: try to roll [%s:%s] back..." % (cmd,step))
                        if cmd in ['add_ip', 'del_ip']:
                            if step==1:
                                self._kick_dhcpd(old_conf.networks, old_conf.addresses, old_conf.private_if)
                        elif cmd in ['add_network']:
                            if step==1:
                                self._do_iptables_restore()
                            elif step==2:
                                vlan = self._diff_key(new_conf.networks, old_conf.networks)
                                if self._find_vlan(new_conf.private_if, str(vlan)):
                                    self._vconfig_del(new_conf.private_if, vlan)
                            elif step==3:
                                self._kick_dhcpd(old_conf.networks, old_conf.addresses, old_conf.private_if)
                        elif cmd in ['del_network']:
                            if step==1:
                                vlan = self._diff_key(old_conf.networks, new_conf.networks)
                                network = old_conf.networks[vlan]
                                if not self._find_vlan(old_conf.private_if, str(vlan)):
                                    self._vconfig_add(old_conf.private_if, vlan)
                                    self._link_up(old_conf.private_if + '.' + str(vlan))
                                    self._ip_add(old_conf.private_if + '.' + str(vlan), network.dhcp, str(self._mask2bit(network["network"])))
                                    self._link_up(old_conf.private_if)
                            elif step==2:
                                self._do_iptables_restore()
                            elif step==4:
                                self._kick_dhcpd(old_conf.networks, old_conf.addresses, old_conf.private_if)
                        elif cmd in ['add_nat']:
                            if step==1:
                                public = self._diff_key(new_conf.publicips, old_conf.publicips)
                                self._ip_del(old_conf.public_if, public, old_conf.public_netmask)
                            elif step==2:
                                self._do_iptables_restore()
                        elif cmd in ['del_nat']:
                            if step==1:
                                self._do_iptables_restore()
                            elif step==2:
                                public = self._diff_key(old_conf.publicips, new_conf.publicips)
                                self._ip_add(old_conf.public_if, public, old_conf.public_netmask)
#                        elif cmd in ['add_instance_bridge']:
#                            if step==1:
#                                vlan = self._diff_key(new_conf.networks, old_conf.networks)
#                                if self._find_vlan(old_conf.public_if, str(vlan)):
#                                    self._link_down(old_conf.public_if + '.' + str(vlan))
#                                    self._vconfig_del(old_conf.public_if, vlan)
#                                if self._find_bridge(bridge_prefix_name + str(vlan)):
#                                    self._link_down(bridge_prefix_name + str(vlan))
#                                    self._brctl_delbr(vlan, bridge_prefix_name)
#                        elif cmd in ['del_instance_bridge']:
#                            if step==1:
#                                vlan = self._diff_key(old_conf.networks, new_conf.networks)
#                                if not self._find_vlan(old_conf.public_if, str(vlan)):
#                                    self._vconfig_add(old_conf.public_if, vlan)
#                                    self._link_up(old_conf.public_if + '.' + str(vlan))
#                                if not self._find_bridge(bridge_prefix_name + str(vlan)):
#                                    self._brctl_addbr(vlan, bridge_prefix_name)
#                                    self._link_up(bridge_prefix_name + str(vlan))
                    except:
                        msg = "_roll_back: rollback is failed at [%s:%s]." % (cmd,step)
                        self._logger.error(msg)
                        raise NwException('_roll_back', msg)
                    else:
                        self._logger.debug("_roll_vack: [%s:%s] is rolled back." % (cmd,step))
                        step -= 1
                self._logger.debug("_roll_vack: rollback succeeded")
        finally:
            return res_conf


    def _diff_key(self, after, before):
        difkey = set(after.keys()) - set(before.keys())
        if len(difkey) != 1:
            if len(difkey) == 0:
                msg = "_diff_key: new key doesn't exist in dict"
            else:
                msg = "_diff_key: too many new keys exists in dict. "
            self._logger.error(msg)
            raise NwException('_diff_key', msg)
        else:
            return difkey.pop()


    # handlers

    def add_ip(self, ip, mac, nodump=None):
        """仮想マシン追加
引数:
 1. VMインスタンスに与えられるプライベートIPアドレス
 2. VMインスタンスが持つMACアドレス
 3. nodump 1の時：Configファイルのダンプをキャンセルする
効果: DHCPのIPアドレス配布情報に、引数のIPアドレスとMACアドレスの組を追加する。
処理概要:
 1. 共有設定オブジェクトをロードする。
 2. 初期化（もしくは初期化状態チェック）
 3. 引数を組としたデータが、共有設定オブジェクト内に重複して存在してないか確認する。存在していれば例外を上げる。
 4. 共有設定オブジェクトに、引数のIPアドレスとMACアドレスの組情報を追加する。
 5. 共有設定オブジェクト内の情報から、DHCPデーモン設定ファイルを再作成する。
 6. DHCPデーモンを再起動する。
 7. 共有設定オブジェクトをダンプする。
例:
  nw = NwHandler()
  nw.add_ip('192.168.1.1', '00:AA:BB:CC:DD:00')
失敗時の対応: 実行中に処理が失敗した場合、共有設定オブジェクトと、マシンの状態は可能な限りメソッド実行前に戻される。
        """
        self._logger.info("add_ip is called")
        rc = False
        try:
            conf = self._lp.__enter__()
            try:
                # check address.
                if conf.addresses.has_key(ip):
                    msg = "add_ip: %s is already used." % ip
                    self._logger.error(msg)
                    raise NwException('add_ip', msg)
                else:
                    conf.addresses.update({ip: mac})
                    # update information for dhcpd.conf
                    try:
                        step = 1
                        self._logger.debug("add_ip:%s: dhcp restarting...." % step)
                        # restart dhcpd
                        self._kick_dhcpd(conf.networks, conf.addresses, conf.private_if)
                    except:
                        msg = "add_ip:%s: dhcpd restarting fails." % step
                        self._logger.error(msg)
                        raise NwException('add_ip', msg, step)
            except:
                if not self._lp.__exit__(*sys.exc_info()):
                    msg = "add_ip is failed"
                    self._logger.exception(msg)
                    raise NwException('add_ip', msg)
            else:
                self._logger.info("add_ip: add_ip succeed (%s , %s)." % (ip, mac) )
                self._lp.__exit__(nodump)
                rc = True
        finally:
            self._logger.info("add_ip returns %s" % rc)
            return rc


    def del_ip(self, ip, mac, nodump=None):
        """仮想マシン削除
引数:
 1. VMインスタンスに与えられるプライベートIPアドレス
 2. VMインスタンスが持つMACアドレス
 3. nodump 1の時：Configファイルのダンプをキャンセルする
効果: DHCPのIPアドレス配布情報から、引数のIPアドレスとMACアドレスの組を削除する。
処理概要:
 1. 共有設定オブジェクトをロードする。
 2. 初期化（もしくは初期化状態チェック）
 3. 引数を組としたデータが、共有設定オブジェクト内に存在しているか確認する。存在しなければ例外を上げる。
 4. 共有設定オブジェクトから、引数のIPアドレスとMACアドレスの組情報を削除する。
 5. 共有設定オブジェクト内の情報から、DHCPデーモン設定ファイルを再作成する。
 6. DHCPデーモンを再起動する。
 7. 共有設定オブジェクトをダンプする。
例:
  nw = NwHandler()
  nw.del_ip('192.168.1.1', '00:AA:BB:CC:DD:00')
失敗時の対応: 実行中に処理が失敗した場合、共有設定オブジェクトと、マシンの状態は可能な限りメソッド実行前に戻される。
        """
        self._logger.info("del_ip is called")
        rc = False
        try:
            conf = self._lp.__enter__()
            try:
                # check address.
                if not conf.addresses.has_key(ip):
                    msg = "del_ip: %s is not used." % ip
                    self._logger.error(msg)
                    raise NwException('del_ip', msg)
                else:
                    del conf.addresses[ip]
                    # update information for dhcpd.conf
                    try:
                        step = 1
                        self._logger.debug("del_ip:%s: dhcp restarting...." % step)
                        # restart dhcpd
                        self._kick_dhcpd(conf.networks, conf.addresses, conf.private_if)
                    except:
                        msg = "del_ip:%s: dhcpd restarting fails." % step
                        self._logger.error(msg)
                        raise NwException('del_ip', msg, step)
            except:
                if not self._lp.__exit__(*sys.exc_info()):
                    msg = "del_ip is failed"
                    self._logger.exception(msg)
                    raise NwException('del_ip', msg)
            else:
                self._logger.info("del_ip: del_ip succeed (%s , %s)." % (ip, mac) )
                self._lp.__exit__(nodump)
                rc = True
        finally:
            self._logger.info("del_ip returns %s" % rc)
            return rc


    def add_network(self, vlan, network, netmask, broadcast, gateway, nameserver, dhcp, username, nodump=None):
        """仮想ネットワーク追加
引数:
 1. VLAN ID
 2. VLANに与えられるネットワークアドレス
 3. VLANに与えられるネットワークのネットマスク
 4. VLANに与えられるネットワークのブロードキャストアドレス
 5. VLANに与えられるネットワークのゲートウェイアドレス
 6. VLANに与えられるネットワークのDNSサーバアドレス
 7. VLANに与えられるネットワークのDHCPサーバアドレス
 8. VLANと対応される"ユーザ名-グループ名"文字列
 9. nodump 1の時：Configファイルのダンプをキャンセルする
効果: VLANごとのネットワーク環境の追加
処理概要:
 1. 共有設定オブジェクトをロードする。
 2. 初期化（もしくは初期化状態チェック）
 3. 引数のVLAN IDが、共有設定オブジェクト内に重複して存在してないか確認する。存在していれば例外を上げる。
 4. NW管理サーバ上のnetfilterにユーザ名-グループ名のチェインを作成し、VLANのネットワークのパケットが転送されるようにする。以下のコマンドを実行する。
     iptables -t filter -N ユーザ名-セキュリティグループ名
     （もし既にチェインが存在していれば、右のコマンドを10回実行（この動作はEuclyptusの実装と同じ）"iptables -t filter -D FORWARD -j ユーザ名-グループ名"）
     iptables -t filter -A FORWARD -j ユーザ名-グループ名
     iptables -t filter -A FORWARD -s VLANのネットワーク/ネットマスク -d VLANのネットワーク/ネットマスク -j ACCEPT
 5. NW管理サーバ上に、VLANインターフェイスを作成する。以下のコマンドを実行する。
     vconfig add プライベートIF "VLAN ID"
 6. 作成したインターフェイスを活性化する。以下のコマンドを実行する。
     ip link set dev プライベートIF."VLAN ID" up
 7. VLANインターフェイスに、VLANのネットワークのゲートウェイアドレスを付与する。以下のコマンドを実行する。
     ip addr add ゲートウェイアドレス/ネットマスク broadcast ブロードキャストアドレス dev プライベートIF
     ip link set dev プライベートIF up
 8. 共有設定オブジェクトに、VLAN IDごとのネットワークパラメータの情報を追加する。
 9. 共有設定オブジェクト内の情報から、DHCPデーモン設定ファイルを再作成する。
 10. DHCPデーモンを再起動する。
 11. 共有設定オブジェクトをダンプする。
例:
  nw = NwHandler()
  nw.add_network(10, '192.168.1.0', '255.255.255.0', '192.168.1.255', '192.168.1.254', '192.168.255.16', 'test01-default')
失敗時の対応: 実行中に処理が失敗した場合、共有設定オブジェクトと、マシンの状態は可能な限りメソッド実行前に戻される。
        """
        self._logger.info("add_network is called")
        rc = False
        try:
            conf = self._lp.__enter__()
            try:
                # check VLAN ID
                if conf.networks.has_key(vlan):
                    msg = "add_network %d is already used." % vlan
                    self._logger.error(msg)
                    raise NwException('add_network', msg)
                else:
                    # update information for dhcpd.conf
                    conf.networks.update({int(vlan): {'network': network, 'netmask': netmask, 'broadcast': broadcast, 'gateway': gateway, 'nameserver': nameserver, 'dhcp': dhcp, 'username': username}})

                    try:
                        step = 1
                        # create nefilter chain of user name
                        self._logger.debug("add_network:%s: netfilter setting starts." % step)
                        if not self._check_chain(username):
                            self._put_iptables('filter', ['-N', username])
                        else:
                            count = 0
                            # same handling with eucalyptus. I don't know what mean.
                            while (count < 10):
                                try:
                                    self._put_iptables('filter', ['-D', 'FORWARD', '-j', username])
                                except:
                                    pass
                                else:
                                    self._logger.warning('add_network: chain(%s) already exists.' % username )
                                count = count + 1
                        # user name chain and network add FORWARD chain
                        self._put_iptables('filter', ['-A', 'FORWARD', '-j', username])
                        self._put_iptables('filter', ['-A', 'FORWARD', '-s', network + '/' + netmask, '-d', network + '/' + netmask, '-j', 'ACCEPT'])
                    except:
                        msg = "add_network:%s: etfilter setting fails." % step
                        self._logger.error(msg)
                        raise NwException('add_network', msg, step)
                    else:
                        self._logger.debug("add_network:%s: netfilter setting done." % step)
                        # network interface setting. create VLAN interface and put dhcp address.
                        try:
                            step=2
                            self._logger.debug("add_network:%s: network interface setting starts." % step)
                            self._vconfig_add(conf.private_if, vlan)
                            self._link_up(conf.private_if + '.' + str(vlan))
                            self._ip_add(conf.private_if + '.' + str(vlan), dhcp, str(self._mask2bit(netmask)))
                            self._link_up(conf.private_if)
                        except:
                            msg = "add_network:%s: network interface setting fails." % step
                            self._logger.error(msg)
                            raise NwException('add_network', msg, step)
                        else:
                            self._logger.debug("add_network:%s: network interface setting succeed." % step)
                            # dhcpd restarting
                            try:
                                step=3
                                self._logger.debug("add_network:%s: dhcp restarting...." % step)
                                self._kick_dhcpd(conf.networks, conf.addresses, conf.private_if)
                            except:
                                msg = "add_network:%s: dhcp restarting fails." % step
                                self._logger.error(msg)
                                raise NwException('add_network', msg, step)
            except:
                if not self._lp.__exit__(*sys.exc_info()):
                    msg = "add_network is failed"
                    self._logger.exception(msg)
                    raise NwException('add_network', msg)
            else:
                conf.netfilter = self._do_iptables_save()
                self._logger.info( "add_network: add_network succeed.")
                self._lp.__exit__(nodump)
                rc = True
        finally:
            self._logger.info("add_network returns %s" % rc)
            return rc


    def del_network(self, vlan, nodump=None):
        """ 仮想ネットワーク削除
引数:
 1. VLAN ID
 2. nodump 1の時：Configファイルのダンプをキャンセルする
効果: VLANごとのネットワーク環境の削除
処理概要:
 1. 共有設定オブジェクトをロードする。
 2. 初期化（もしくは初期化状態チェック）
 3. 引数のVLAN IDが、共有設定オブジェクト内に存在していることを確認する。存在していなければ例外を上げる。
 4. NW管理サーバ上の、VLANインターフェイスを非活性化する。以下のコマンドを実行する。
     ip link set dev "プライベートIF名.引数vlanで指定されたVLAN ID"  down
 5. NW管理サーバ上のnetfilterにユーザ名-グループ名のチェインを削除する。以下のコマンドを実行する。
     iptables -t filter -D FORWARD -s "VLANのネットワークアドレス"/ネットマスク" -d "VLANのネットワークアドレス"/ネットマスク" -j ACCEPT"
 6. NW管理サーバ上の、VLANインターフェイスを削除する。以下のコマンドを実行する。
     vconfig rem "プライベートIF名.引数vlanで指定されたVLAN ID"
 7. VLANインターフェイスから、VLANのネットワークのゲートウェイアドレスを削除する。以下のコマンドを実行する（VLANインターフェイスが削除された時点で、それに振られたアドレスも消えているので、このコマンドは必ず失敗するはずである。コマンドの結果が失敗しても先の処理に進む（この動作はEuclyptusの実装と同じ））
     ip addr del ゲートウェイアドレス/ネットマスク broadcast ブロードキャストアドレス dev デバイス名
     iptables -t filter -D FORWARD -j ユーザ名-グループ名
     iptables -t filter -F ユーザ名-グループ名
     iptables -t filter -X ユーザ名-グループ名
 8. 共有設定オブジェクトから、VLAN IDに対応した情報を削除する。
 9. 共有設定オブジェクト内の情報から、DHCPデーモン設定ファイルを再作成する。
 10. DHCPデーモンを再起動する。
 11. 共有設定オブジェクトをダンプする。
例:
  nw = NwHandler()
  nw.del_network(10)
失敗時の対応: 実行中に処理が失敗した場合、共有設定オブジェクトと、マシンの状態は可能な限りメソッド実行前に戻される。
        """
        self._logger.info("del_network is called")
        rc = False
        try:
            conf = self._lp.__enter__()
            try:
                if not conf.networks.has_key(vlan):
                    self._logger.error("del_network: %s is not used." % str(vlan))
                    raise NwException('del_network', "%s is not used." % str(vlan))
                else:
                    network = conf.networks[vlan]['network']
                    netmask = conf.networks[vlan]['netmask']
                    dhcp = conf.networks[vlan]['dhcp']
                    username = conf.networks[vlan]['username']
#                    broadcast = conf.networks[vlan]['broadcast']
                    # update information for dhcpd.conf
                    del conf.networks[vlan]
                    try:
                        step=1
                        self._logger.debug("del_network:%s: network interface setting starts." % step)
                        # delete VLAN interface
                        self._link_down(conf.private_if + '.' + str(vlan))
                        self._vconfig_del(conf.private_if, vlan)
                    except:
                        msg = 'add_network: network interface setting fails.'
                        self._logger.error(msg)
                        raise NwException('add_network', msg, step)
                    else:
                        self._logger.debug("del_network:%s: network interface setting done." % step)
                        try:
                            step=2
                            self._logger.debug("del_network:%s: netfilter setting(1) starts." % step)
                            # delete netfilter rule (delete network from FORWARD chain)
                            self._put_iptables('filter', ['-D', 'FORWARD', '-s', network + '/' + netmask, '-d', network + '/' + netmask, '-j', 'ACCEPT'])
                        except:
                            msg = "add_network:%s: netfilter setting(1) fails." % step
                            self._logger.error(msg)
                            raise NwException('add_network', msg, step)
                        else:
                            self._logger.debug("del_network:%s: netfilter setting(1) done." % step)
                            try:
                                step=3
                                self._logger.debug("del_network:%s: netfilter setting(2) starts." % step)
                                # remove dhcp address from VLAN interface, Maybe it fails, cause VLAN interface alread removed. same handling with Eucalyptus
                                self._ip_del(conf.private_if + '.' + str(vlan), dhcp, str(self._mask2bit(netmask)))
                            except:
                                self._logger.warning("del_network:%s: removing dhcp address failed. (maybe interface already removed)" % step)
                            # Delete user name chain from netfilter
                            try:
                                if self._check_chain(username):
                                    self._put_iptables('filter', ['-D', 'FORWARD', '-j', username])
                                self._put_iptables('filter', ['-F', username])
                                self._put_iptables('filter', ['-X', username])
                            except:
                                msg = "add_network:%s: netfilter setting(2) fails." % step
                                self._logger.error(msg)
                                raise NwException('add_network', msg, step)
                            else:
                                self._logger.debug("del_network:%s: netfilter setting(2) done." % step)
                                try:
                                    step=4
                                    self._logger.debug("del_network:%s: dhcp restarting...." % step)
                                    # restarting dhcpd
                                    self._kick_dhcpd(conf.networks, conf.addresses, conf.private_if)
                                except:
                                    # if there is no network, dhcpd may fails. but condone it.
                                    if len(conf.networks) == 0:
                                        self._logger.warning("del_network:%s: dhcpd stops, because we have no network now." % step)
                                    else:
                                        msg = "add_network:%s: dhcp restarting fails." % step
                                        self._logger.error(msg)
                                        raise NwException('add_network', msg, step)
            except:
                if not self._lp.__exit__(*sys.exc_info()):
                    msg = "del_network is failed"
                    self._logger.exception(msg)
                    raise NwException('del_network', msg)
            else:
                conf.netfilter = self._do_iptables_save()
                self._logger.info( "del_network: del_network succeed.")
                self._lp.__exit__(nodump)
                rc = True
        finally:
            self._logger.info("del_network returns %s" % rc)
            return rc


    def add_nat(self, private, public, nodump=None):
        """パブリックIP追加
引数:
 1. プライベートアドレス
 2. パブリックアドレス
 3. nodump 1の時：Configファイルのダンプをキャンセルする
効果: 外部からアクセス可能なパブリックアドレスと、VMインスタンスの使用するプライベートアドレスをNATで対応付ける。
処理概要:
 1. 共有設定オブジェクトをロードする。
 2. 初期化（もしくは初期化状態チェック）
 3. 引数のパブリックアドレスが使用されているか、共有設定オブジェクトを確認する。使用されていれば例外を上げる。
 4. NW管理サーバのパブリックIFに、パブリックアドレスを付与する。以下のコマンドを実行する。
     ip addr add "パブリックアドレス"/32 dev "パブリックIF名"
 5. NW管理サーバのnetfilterにNATルールを追加する。以下のコマンドを実行する。
     iptables -t nat -A PREROUTING -d "パブリックアドレス" -j DNAT --to-destination "プライベートアドレス"
     iptables -t nat -A OUTPUT -d "パブリックアドレス" -j DNAT --to-destination "プライベートアドレス"
     iptables -t nat -I POSTROUTING -s "プライベートアドレス" ! -d "NW管理サーバのネットワークアドレス"/"ネットマスク" -j SNAT --to-source "パブリックアドレス"
 6. 共有設定オブジェクトに、パブリックアドレスを使用中アドレスとして追加する。
 7. 共有設定オブジェクトの、netfilterの設定を更新する。"iptables-save"で出力される文字列を記録する。
 8. 共有設定オブジェクトをダンプする。
例:
  nw = NwHandler()
  nw.add_nat('192.168.1.1', '172.30.112.168')
失敗時の対応: 実行中に処理が失敗した場合、共有設定オブジェクトと、マシンの状態は可能な限りメソッド実行前に戻される。
        """
        self._logger.info("add_nat is called")
        rc = False
        try:
            conf = self._lp.__enter__()
            try:
                if conf.publicips.has_key(public):
                    self._logger.error("add_nat %s is already used." % public)
                    raise NwException('add_nat', "%s is already used." % public)
                else:
                    # save public/private ip information
                    conf.publicips.update({public: private})
                    try:
                        step = 1
                        self._logger.debug("add_nat:%s: network interface setting starts." % step)
                        # put public address to public interface
                        self._ip_add(conf.public_if, public, conf.public_netmask)
                    except:
                        msg = "add_nat:%s: network interface setting fails." % step
                        self._logger.error(msg)
                        raise NwException('add_nat', msg, step)
                    else:
                        self._logger.debug("add_nat:%s: network interface setting done." % step)
                        try:
                            step = 2
                            self._logger.debug("add_nat:%s: netfilter setting starts." % step)
                            # set nat rule to netfilter nat table
                            self._put_iptables('nat', ['-A', 'PREROUTING', '-d', public, '-j', 'DNAT', '--to-destination', private])
                            self._put_iptables('nat', ['-A', 'OUTPUT', '-d', public, '-j', 'DNAT', '--to-destination', private])
                            self._put_iptables('nat', ['-I', 'POSTROUTING', '-s', private, '!', '-d', conf.private_network + '/' + conf.private_netmask, '-j', 'SNAT', '--to-source', public])
                        except:
                            msg = "add_nat:%s: netfilter setting fails." % step
                            self._logger.error(msg)
                            raise NwException('add_nat', msg, step)
            except:
                if not self._lp.__exit__(*sys.exc_info()):
                    msg = "add_nat is failed"
                    self._logger.exception(msg)
                    raise NwException('add_nat', msg)
            else:
                self._logger.debug('add_nat: netfilter setting done.')
                # save current netfilter setting
                conf.netfilter = self._do_iptables_save()
                self._logger.info( 'add_nat: add_nat succeed.')
                self._lp.__exit__(nodump)
                rc = True
        finally:
            self._logger.info("add_nat returns %s" % rc)
            return rc


    def del_nat(self, private, public, nodump=None):
        """パブリックIP削除
引数:
 1. プライベートアドレス
 2. パブリックアドレス
 3. nodump 1の時：Configファイルのダンプをキャンセルする
効果: 外部からアクセス可能なパブリックアドレスと、VMインスタンスの使用するプライベートアドレスのNATでの対応付けを削除する。
処理概要:
 1. 共有設定オブジェクトをロードする。
 2. 初期化（もしくは初期化状態チェック）
 3. 引数のパブリックアドレスが使用されているか、共有設定オブジェクトを確認する。使用されていなければ例外を上げる。
 4. NW管理サーバのnetfilterにNATルールを削除する。以下のコマンドを実行する。
     iptables -t nat -D PREROUTING -d "パブリックアドレス" -j DNAT --to-destination "プライベートアドレス"
     iptables -t nat -D OUTPUT -d "パブリックアドレス" -j DNAT --to-destination "プライベートアドレス"
     iptables -t nat -D POSTROUTING -s "プライベートアドレス" ! -d "NW管理サーバのネットワークアドレス"/"ネットマスク" -j SNAT --to-source "パブリックアドレス"
 5. NW管理サーバのパブリックIFから、パブリックアドレスを削除する。以下のコマンドを実行する。
     ip addr del "パブリックアドレス"/32 dev "パブリックIF名"
 6. 共有設定オブジェクトの使用中アドレスからパブリックアドレスを削除する。
 7. 共有設定オブジェクトのnetfilterの設定を更新する。"iptables-save"で出力される文字列を記録する。
 8. 共有設定オブジェクトをダンプする。
例:
  nw = NwHandler()
  nw.add_nat('192.168.1.1', '172.30.112.168')
失敗時の対応: 実行中に処理が失敗した場合、共有設定オブジェクトと、マシンの状態は可能な限りメソッド実行前に戻される。
        """
        self._logger.info("del_nat is called")
        rc = False
        try:
            conf = self._lp.__enter__()
            try:
                if not conf.publicips.has_key(public):
                    self._logger.error("del_nat %s is not used." % public)
                    raise NwException('del_nat', "%s is not used." % public)
                else:
                    # save public/private ip information
                    del conf.publicips[public]
                    try:
                        step = 1
                        self._logger.debug("del_nat:%s: netfilter setting starts." % step)
                        # delete nat rules from netfilter nat table
                        self._put_iptables('nat', ['-D', 'PREROUTING', '-d', public, '-j', 'DNAT', '--to-destination', private])
                        self._put_iptables('nat', ['-D', 'OUTPUT', '-d', public, '-j', 'DNAT', '--to-destination', private])
                        self._put_iptables('nat', ['-D', 'POSTROUTING', '-s', private, '!', '-d', conf.private_network + '/' + conf.private_netmask, '-j', 'SNAT', '--to-source', public])
                    except:
                        msg = "del_nat:%s: network netfilter setting fails." % step
                        self._logger.error(msg)
                        raise NwException('del_nat', msg, step)
                    else:
                        self._logger.debug("del_nat:%s: netfilter setting done." % step)
                        try:
                            self._logger.debug("del_nat:%s: network interface setting starts." % step)
                            # delete public ip from public interface
                            self._ip_del(conf.public_if, public, conf.public_netmask)
                        except:
                            msg = 'del_nat: network interface setting fails.'
                            self._logger.error(msg)
                            raise NwException('del_nat', msg, step)
            except:
                if not self._lp.__exit__(*sys.exc_info()):
                    msg = "add_nat is failed"
                    self._logger.exception(msg)
                    raise NwException('add_nat', msg)
            else:
                self._logger.debug('del_nat: network interface setting starts.')
                # save current netfilter setting
                conf.netfilter = self._do_iptables_save()
                self._logger.info( 'del_nat: add_nat succeed.')
                self._lp.__exit__(nodump)
                rc = True
        finally:
            self._logger.info("del_nat returns %s" % rc)
            return rc


    def set_filter(self, filter_type, my_username, other_username=False, other_net=False, protocol=False, minport=False, maxport=False, nodump=None):
        """パーミッション追加・削除
引数:
 1. フィルタタイプ（"open"もしくは"close"）
 2. 接続先となるユーザ名
 3. nodump 1の時：Configファイルのダンプをキャンセルする
キーワード引数
 other_username: 接続元となるユーザ名（デフォルト: False）
 other_net: 接続元となるネットワーク（デフォルト: False）
 protocol: プロトコル名（デフォルト: False）
 minport: ポート範囲最小値（デフォルト: False）
 maxport: ポート範囲最大値（デフォルト: False）
効果: 接続先となるユーザ名に対するフィルタリング設定を操作する。
処理概要:
 1. 共有設定オブジェクトをロードする。
 2. 初期化（もしくは初期化状態チェック）
 3. 引数の解析: 共有設定オブジェクトのデータから、接続先ユーザ名と対応するネットワーク情報を取得する。
 4. 引数の解析: 共有設定オブジェクトのデータから、接続元ユーザ名と対応するネットワーク情報を取得する。other_usernameキーワードが指定していなければ、other_netを接続元ネットワークとする。
 5. 解析した引数から、"iptables"のコマンドラインを生成する
 6. "iptables"を実行する
 7. 共有設定オブジェクトのnetfilterの設定を更新する。"iptables-save"で出力される文字列を記録する。
 8. 共有設定オブジェクトをダンプする。
例:
  nw = NwHandler()
  nw.set_filter('open', 'test01-default', other_username='test02-default', protocol='tcp', minport='21', maxport='22')
  nw.set_filter('open', 'test01-default', other_net='192.168.3.0/24', protocol='tcp', minport='21', maxport='22')
  nw.set_filter('close', 'test01-default', other_username='test02-default', protocol='tcp', minport='21', maxport='22')
  nw.set_filter('close', 'test01-default', other_net='192.168.3.0/24', protocol='tcp', minport='21', maxport='22')
失敗時の対応: 実行中に処理が失敗した場合、共有設定オブジェクトと、マシンの状態は可能な限りメソッド実行前に戻される。
        """
        self._logger.info("set_filter is called")
        rc = False
        try:
            conf = self._lp.__enter__()
            try:
                if filter_type == 'open':
                    args = ['-A', my_username,]
                elif filter_type == 'close':
                    args = ['-D', my_username,]
                else:
                    raise NwException('set_filter', "filter type invalid (%s)." % filter_type)

                src_net = ''
                # resolve network informaion by user name
                for i in conf.networks.keys():
                    if conf.networks[i]['username'] == my_username:
                        src_net = conf.networks[i]['network'] + '/' + conf.networks[i]['netmask']

                dst_net = ''
                if other_username != False or other_net != False:
                    if other_username:
                        for i in conf.networks.keys():
                            if conf.networks[i]['username'] == other_username:
                                dst_net = conf.networks[i]['network'] + '/' + conf.networks[i]['netmask']
                    else:
                        dst_net = other_net
                else:
                    raise NwException('set_filter', 'destination network invalid.')

                args = args + ['-s', src_net, '-d', dst_net]

                if protocol:
                    args = args + ['-p', protocol]
                if minport and maxport and protocol == 'tcp' or protocol == 'udp':
                    args = args + ['--dport', minport + ':' + maxport]

                args = args + ['-j', 'ACCEPT']

                self._logger.debug('set_filter: netfilter setting starts.')
                # put netfilter rule
                try:
                    self._put_iptables('filter',args)
                except:
                    self._logger.error('set_filter: iptables fails.')
                    raise NwException('set_filter', 'iptables fails.')

            except:
                if not self._lp.__exit__(*sys.exc_info()):
                    msg = "set_filter is failed"
                    self._logger.exception(msg)
                    raise NwException('set_filter', msg)
            else:
                self._logger.debug('set_filter: netfilter setting done.')
                # save current netfilter setting
                conf.netfilter = self._do_iptables_save()
                self._logger.info( 'set_filter: set_filter succeed.')
                self._lp.__exit__(nodump)
                rc = True
        finally:
            self._logger.info("set_filter returns %s" % rc)
            return rc


    def add_instance_bridge(self, vlan, bridge_prefix_name, nodump=None):
        """ VLANインターフェイスとブリッジの作成
引数:
 1. VLAN ID
 2. ブリッジ名のprefix
 3. nodump 1の時：Configファイルのダンプをキャンセルする
効果: VLANインターフェイスの生成と、そのインターフェイスへのブリッジの作成。ブリッジ名は第2引数の文字列とVLAN IDを繋げたもの。このメソッドは、NW管理サーバ上ではなく、HyperVisor上で実行されることを意図している。そのため、共有設定オブジェクトへの設定のダンプ（状態保存）は行わず、インターフェイス名の取得だけのために、共有設定オブジェクトをロードしている。
処理概要
 1. 共有設定オブジェクトをロードする。
 2. サーバ上に、VLANインターフェイスを作成する。以下のコマンドを実行する。
     vconfig add パブリックIF "VLAN ID"
 3. サーバ上に、ブリッジを作成する。以下のコマンドを実行する。
     brctl addbr 第2引数の文字列"VLAN ID"
 4. VLANインターフェイスとブリッジを接続する。以下のコマンドを実行する。
     brctl addif 第2引数の文字列"VLAN ID" パブリックIF "VLAN ID"
 5. 作成したインターフェイスを活性化する。以下のコマンドを実行する。
     ip link set dev パブリックIF."VLAN ID" up
 6. 作成したインターフェイスを活性化する。以下のコマンドを実行する。
     ip link set dev 第2引数の文字列"VLAN ID" up
例:
  nw = NwHandler()
  nw.add_instance_bridge(20, 'hogebr')
失敗時の対応: 実行中に処理が失敗した場合、マシンの状態は可能な限りメソッド実行前に戻される。
        """
        self._logger.info("add_instance_bridge is called")
        rc = False
        try:
            conf = self._lp.__enter__()
            try:
                try:
                    step = 1
                    self._vconfig_add(conf.public_if, vlan)
                    self._brctl_addbr(vlan, bridge_prefix_name)
                    self._brctl_addif(vlan, bridge_prefix_name + str(vlan), conf.public_if + '.' + str(vlan))
                    self._link_up(conf.public_if + '.' + str(vlan))
                    self._link_up(bridge_prefix_name + str(vlan))
                except:
                    msg = "add_instance_bridge:%s: network interface setting fails." % step
                    self._logger.error(msg)
                    raise NwException('add_instance_bridge', msg, step)
            except:
                if not self._lp.__exit__(*sys.exc_info()):
                    msg = "add_instance_bridge is failed"
                    self._logger.exception(msg)
                    raise NwException('add_instance_bridge', msg)
            else:
                self._logger.info( 'add_instance_bridge: add_instance_bridge succeed.')
                self._lp.__exit__(nodump)
                rc = True
        finally:
            self._logger.info("add_instance_bridge returns %s" % rc)
            return rc


    def del_instance_bridge(self, vlan, bridge_prefix_name, nodump=None):
        """ VLANインターフェイスとブリッジの削除
引数:
 1. VLAN ID
 2. ブリッジ名のprefix
 3. nodump 1の時：Configファイルのダンプをキャンセルする
効果: VLANインターフェイスの削除と、そのインターフェイスへ接続されていたブリッジの削除。ブリッジ名は第2引数の文字列とVLAN IDを繋げたもの。このメソッドは、NW管理サーバ上ではなく、HyperVisor上で実行されることを意図している。そのため、共有設定オブジェクトへの設定のダンプ（状態保存）は行わず、インターフェイス名の取得だけのために、共有設定オブジェクトをロードしている。
処理概要
 1. 共有設定オブジェクトをロードする。
 2. インターフェイスを非活性化する。以下のコマンドを実行する。
     ip link set dev パブリックIF."VLAN ID" down
 3. インターフェイスを非活性化する。以下のコマンドを実行する。
     ip link set dev 第2引数の文字列"VLAN ID" down
 4. VLANインターフェイスを削除する。以下のコマンドを実行する。
     vconfig rem パブリックIF "VLAN ID"
 5. ブリッジを削除する。以下のコマンドを実行する。
     brctl delbr 第2引数の文字列"VLAN ID"
例:
  nw = NwHandler()
  nw.del_instance_bridge(20, 'hogebr')
失敗時の対応: 実行中に処理が失敗した場合、マシンの状態は可能な限りメソッド実行前に戻される。
        """
        self._logger.info("del_instance_bridge is called")
        rc = False
        try:
            conf = self._lp.__enter__()
            try:
                try:
                    # delete VLAN interface and bridge interface.
                    self._link_down(conf.public_if + '.' + str(vlan))
                    self._link_down(bridge_prefix_name + str(vlan))
                    self._vconfig_del(conf.public_if, vlan)
                    self._brctl_delbr(vlan, bridge_prefix_name)
                except:
                    msg = 'del_instance_bridge: network interface setting fails.'
                    self._logger.error(msg)
                    raise NwException('del_instance_bridge', msg)
            except:
                if not self._lp.__exit__(*sys.exc_info()):
                    msg = "del_instance_bridge is failed"
                    self._logger.exception(msg)
                    raise NwException('del_instance_bridge', msg)
            else:
                self._logger.info( 'del_instance_bridge: del_instance_bridge succeed.')
                self._lp.__exit__(nodump)
                rc = True
        finally:
            self._logger.info("del_instance_bridge returns %s" % rc)
            return rc

    def get_config(self):
        """ エージェント設定情報の取得
引数:
 なし
効果: NW設定に具体的な操作は一切しない。そのため、共有設定オブジェクトへの設定のダンプ（状態保存）は行わず、共有設定オブジェクトの取得のみを行う。
処理概要
 1. 共有設定オブジェクトをロードする。
 2. 共通設定オブジェクトを返却する。
例:
  nw = NwHandler()
  nw.get_config
失敗時の対応: 実行中に処理が失敗してもロールバック等は行わない。
        """
        self._logger.info("get_config is called")
        rc = False
        try:
            conf = self._lp.__enter__()
            rc = conf
            self._lp.__exit__(1)
        finally:
            self._logger.info("get_config returns %s" % rc)
            return rc

    # initializer

    def _netfilter_initialize(self, private_network, private_netmask):
        try:
            self._put_iptables('filter', ['-F'])
            self._put_iptables('nat', ['-F'])
            self._put_iptables('filter', ['-P', 'FORWARD', 'DROP'])
            self._put_iptables('filter', ['-A', 'FORWARD', '-m', 'conntrack', '--ctstate', 'ESTABLISHED', '-j', 'ACCEPT'])
            self._put_iptables('filter', ['-A', 'FORWARD', '-s', private_network + '/' + private_netmask, '!', '-d', private_network + '/' + private_netmask, '-j', 'ACCEPT'])
            self._put_iptables('filter', ['-A', 'FORWARD', '!', '-s', private_network + '/' + private_netmask, '-d', private_network + '/' + private_netmask, '-j', 'ACCEPT'])
            self._put_iptables('nat', ['-A', 'POSTROUTING', '!', '-d', private_network + '/' + private_netmask, '-j', 'MASQUERADE'])
        except:
            self._logger.error('_netfilter_initialize: initialize failed (netfilter initail setting fails).')
            raise NwException('_netfilter_initialize', 'initailize failed (dumped config object restore fails)')
        else:
            self._logger.debug('_netfilter_initialize: initialize succeed.')

    # network commands

    def _ip_add(self, interface, address, netmask='32'):
        return self._run([self._iproute, 'addr', 'add', address + '/' + netmask, 'dev', interface])

    def _ip_del(self, interface, address, netmask='32'):
        return self._run([self._iproute, 'addr', 'del', address + '/' + netmask, 'dev', interface])

    def _ip_flush(self, interface, preservePrimary=False):
        if preservePrimary:
            return self._run([self._iproute, 'addr', 'flush', 'dev', interface, 'secondary'])
        else:
            return self._run([self._iproute, 'addr', 'flush', 'dev', interface])

#    def _gateway_ip_add(self, interface, gateway, netmask, broadcast):
#        return self._run([self._iproute, 'addr', 'add', gateway + '/' + str(self._mask2bit(netmask)), 'broadcast', broadcast, 'dev', interface])
#
#    def _gateway_ip_del(self, interface, gateway, netmask, broadcast):
#        return self._run([self._iproute, 'addr', 'del', gateway + '/' + str(self._mask2bit(netmask)), 'broadcast', broadcast, 'dev', interface])

    def _link_up(self, interface):
        return self._run([self._iproute, 'link', 'set', interface, 'up'])

    def _link_down(self, interface):
        return self._run([self._iproute, 'link', 'set', interface, 'down'])

    def _vconfig_add(self, interface, vlan):
        vlan_str = str(vlan)
        if self._find_vlan(interface, vlan_str):
            self._logger.warning('VLAN interface already exists')
        else:
            return self._run([self._vconfig, 'add', interface, vlan_str])

    def _vconfig_del(self, interface, vlan=None):
        if(vlan is not None):
            interface = interface + '.' + str(vlan)
        if self._find_vlan(interface):
            return self._run([self._vconfig, 'rem', interface])
        else:
            self._logger.warning('VLAN interface does not exist')

    def _brctl_addbr(self, vlan, bridge_prefix_name):
        return self._run([self._brctl, 'addbr', bridge_prefix_name + str(vlan)])

    def _brctl_addif(self, vlan, bridge_name, interface):
        if self._find_bridge(bridge_name) and self._find_vlan(interface, str(vlan)):
            return self._run([self._brctl, 'addif', bridge_name, interface + '.' + str(vlan)])
        else:
            self._logger.warning('VLAN interface or bridge interface do not exist')

    def _brctl_delbr(self, vlan, bridge_prefix_name):
        return self._run([self._brctl, 'delbr', bridge_prefix_name + str(vlan)])

    def _find_vlan(self, interface, vlan=None):
        if(vlan is not None):
            interface = interface + '.' + str(vlan)
        return exists('/sys/class/net/' + interface)

    def _find_bridge(self, bridge):
        return exists('/sys/class/net/' + bridge + '/bridge')

    def _put_iptables(self, table, args):
        return self._run([self._iptables, '-t', table] + args)

    def _check_chain(self, chain):
        try:
            self._run([self._iptables, '-t', 'filter', '-L', chain, '-n'])
        except:
            return False
        else:
            return True

    def _get_vlan_devs(self, interface):
        return filter(lambda val: val.split('.')[0]==interface, os.listdir(self._vconfig_dir))

    # DHCP daemon

    def _generate_dhcpd_conf(self, networks, addresses):
        # create dhcpd.conf from Configuration object.
        config_str = "# automatically generated config file for DHCP server\n"
        config_str = config_str + "default-lease-time 1200;\n"
        config_str = config_str + "max-lease-time 1200;\n"
        config_str = config_str + "ddns-update-style none;\n"
        config_str = config_str + "\n"
#        config_str = config_str + "shared-network devnet {\n"
        for i in networks.keys():
            config_str = config_str + "    subnet %s netmask %s {\n" % (networks[i]['network'],  networks[i]['netmask'])
            config_str = config_str + "        option subnet-mask %s;\n" % networks[i]['netmask']
            config_str = config_str + "        option broadcast-address %s;\n" % networks[i]['broadcast']
            config_str = config_str + "        option domain-name-servers %s;\n" % networks[i]['nameserver']
            config_str = config_str + "        option routers %s;\n" % networks[i]['gateway']
            config_str = config_str + "    }\n"
            config_str = config_str + "\n"
        for j in addresses.keys():
            config_str = config_str + "    host node-%s {\n" % j
            config_str = config_str + "        hardware ethernet %s;\n" % addresses[j]
            config_str = config_str + "        fixed-address %s;\n" % j
            config_str = config_str + "    }\n"
#        config_str = config_str + "}"
        return config_str

    def _start_dhcpd(self, dhcpd_cmd):
        try:
            # delete dhcp.trace
            if exists(self._dhcp_trace):
                os.unlink(self._dhcp_trace)
            # create dhcp.leases
            f = open(self._dhcp_leases, 'w')
            f.close()
        except:
            self._logger.error('dhcpd leases or dhcpd trace failed.')
            raise NwException('_start_dhcpd', 'dhcpd leases or dhcpd trace failed.')
        else:
            # start dhcpd
            if self._run(dhcpd_cmd):
                self._logger.debug('dhcpd started.')
                return True
            else:
                self._logger.error('dhcpd starting failed.')
                raise NwException('_restart_dhcpd', 'dhcpd restarting failed.')

    def _stop_dhcpd(self):
        # chekc dhcp.pid
        if exists(self._dhcpd_pid):
            f = open(self._dhcpd_pid, 'r')
            pid =  f.readline().rstrip()
            f.close()
            # check PID of dhcp.pid, Is it number?
            if re.match('\d+', pid):
                # Does PID exist in /proc?
                if exists('/proc/' + pid + '/cmdline'):
                    # Is process dhcpd in /proc?
                    if re.match('.+/dhcpd$', open('/proc/' + pid + '/cmdline').readline().split('\x00')[0]):
                        # kill -9 PID
                        self._run(["kill", "-9", pid])
                        timeout = 30
                        # waiting for shutting down dhcpd (timeout: 30sec)
                        while exists('/proc/' + pid):
                            if timeout == 0:
                                self._logger.error('dhcpd stopping timeout.')
                                raise NwException('_restart_dhcpd', 'dhcpd stopping timeout.')
                            self._logger.debug('waiting for dhcpd is stopping...')
                            time.sleep(1)
                            timeout = timeout -1
                        self._logger.debug('dhcpd was stopped.')
                        return True
                    else:
                        self._logger.warning('dhcpd is not running now. (process is not dhcpd)')
                else:
                    self._logger.warning('dhcpd is not running. (no proceess actually)')
            else:
                self._logger.warning('dhcpd is not running now. (pid file incorrect)')
        else:
            self._logger.warning('dhcpd is not running now. (no pid file)')


    def _stop_and_restart_dhcpd(self, private_if, networks):
        rc = False
        # dhcpd options
        dhcpd_cmd = [self._dhcpd, '-cf', self._dhcp_conf, '-lf', self._dhcp_leases, '-pf', self._dhcpd_pid, '-tf', self._dhcp_trace]
        # interfaces bind by dhcpd.
        dhcpd_cmd = dhcpd_cmd + [private_if + '.' + str(id) for id in networks.keys()]
        # stop dhcpd
        try:
            self._stop_dhcpd()
        except:
            self._logger.error('_stop_and_restart_dhcpd: dhcpd stopping failed.')
            self._logger.error('_stop_and_restart_dhcpd: dhcpd restarting failed.')
            raise NwException('_stop_and_restart_dhcpd', 'dhcpd restarting failed.')
        else:
            # start dhcpd
            try:
                self._start_dhcpd(dhcpd_cmd)
            except:
                if len(networks) == 0:
                    rc = True
                else:
                    self._logger.warning('_stop_and_restart_dhcpd: dhcpd starting failed. We attemp to rollback a configuration.')
                    # for recovery rollback (use old configuration)
                    try:
                        if exists(self._dhcp_conf + '.bak'):
                            os.unlink(self._dhcp_conf)
                            shutil.move(self._dhcp_conf + '.bak', self._dhcp_conf)
                        else:
                            raise
                    except:
                        self._logger.error('_stop_and_restart_dhcpd: dhcpd configuration rollback failed.')
                        raise NwException('_stop_and_restart_dhcpd', 'dhcpd configuration rollback failed.')
                    else:
                        try:
                            self._start_dhcpd(dhcpd_cmd)
                        except:
                            self._logger.error('_stop_and_restart_dhcpd: dhcpd starting with rollbacked configuration failed.')
                            self._logger.error('_stop_and_restart_dhcpd: dhcpd restarting failed.')
                            raise NwException('_stop_and_restart_dhcpd', 'dhcpd restarting failed.')
                        else:
                            self._logger.warning('_stop_and_restart_dhcpd: dhcpd started with rollbacked configuration.')
                            self._logger.error('_stop_and_restart_dhcpd: dhcpd restarting failed.')
                            raise NwException('_stop_and_restart_dhcpd', 'dhcpd restarting failed.')
            else:
                self._logger.debug('_stop_and_restart_dhcpd: _stop_and_restart_dhcpd succeed.')
                return rc


    def _kick_dhcpd(self, networks, addresses, private_if):
        try:
            # create string for dhcpd.conf
            dhcp_conf_str = self._generate_dhcpd_conf(networks, addresses)
            # backup current config file.
            if exists(self._dhcp_conf):
                shutil.copyfile(self._dhcp_conf, self._dhcp_conf + '.bak')
            # writing new configuration
            f = open(self._dhcp_conf, 'w')
            f.write(dhcp_conf_str)
            f.close()
            # if dhcpd launching succeed, delete dhcpd.conf bakcup.
            if self._stop_and_restart_dhcpd(private_if, networks) and exists(self._dhcp_conf + '.bak'):
                os.unlink(self._dhcp_conf + '.bak')
        except:
            msg = '_kick_dhcpd failed.'
            self._logger.error(msg)
            raise NwException('_kick_dhcpd', msg)


    # netfilter dump/undump

    def _do_iptables_save(self):
        rc, netfilter = self._run([self._iptables_save], with_output=True)
        if rc:
            return netfilter
        else:
            raise

    def _do_iptables_restore(self, netfilter):
        rc, new_netfilter = self._run([self._iptables_restore], with_output=True, with_input=netfilter)
        if rc:
            return True
        else:
            raise

    # utils

    def _mask2number(self, mask):
        n = 0
        for i in mask.split('.'):
            n <<= 8
            n += int(i)
        return n

    def _mask2bit(self, mask):
        return int(32 - math.log(0xFFFFFFFF - self._mask2number(mask), 2))

    def _run(self, cmd, with_output=False, with_input=False):
        self._logger.debug(' '.join(cmd))
        proc = subprocess.Popen(cmd, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, close_fds=True)
        if with_input:
            output, error = proc.communicate(with_input)
        else:
            output, error = proc.communicate(None)
        status = proc.wait()
        if os.WIFEXITED(status) or os.WEXITSTATUS(status):

            if len(output):
                for i in output.split("\n"):
                    self._logger.debug(i)

            if len(error):
                for i in error.split("\n"):
                    self._logger.error(i)

            if status == 0:
                if with_output:
                    return (True, output)
                else:
                    return True
            else:
                self._logger.error("'%s' return status: %d", (' '.join(cmd), status))
                raise NwException(cmd, error)
        else:
            self._logger.error("'%s' return status: %d" % (' '.join(cmd), status))
            raise NwException(cmd, error)

 # =====================================================================
 # 
 #    Copyright 2011 NTT Sofware Corporation
 # 
 #    Licensed under the Apache License, Version 2.0 (the "License");
 #    you may not use this file except in compliance with the License.
 #    You may obtain a copy of the License at
 # 
 #        http://www.apache.org/licenses/LICENSE-2.0
 # 
 #    Unless required by applicable law or agreed to in writing, software
 #    distributed under the License is distributed on an "AS IS" BASIS,
 #    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 #    See the License for the specific language governing permissions and
 #    limitations under the License.
 # 
 # =====================================================================
