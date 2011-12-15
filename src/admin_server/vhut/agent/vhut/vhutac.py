# -*- coding: utf-8 -*-
'''
Copyright 2011 NTT Software Corporation.
All Rights Reserved.

@author NTT Software Corporation.
@version 1.0.0

$Date: 2010-08-31 09:54:14 +0900 (火, 31 8 2010) $
$Revision: 435 $
$Author: NTT Software Corporation. $
'''
import os
import sys
from optparse import OptionParser
from nw import NwHandler
import csv
import logging.config

#キー
KEY_ENV_VHUT_HOME = 'VHUT_HOME'
#設定ファイル
PATH_CONFIG = os.environ[KEY_ENV_VHUT_HOME]+"/agent/conf/vhuta.conf"
#NW設定DBファイル
PATH_DATA_NW = os.environ[KEY_ENV_VHUT_HOME]+"/agent/data/nw.db"

def main():
    usage = "%prog --action ACTION [parameter options]"
    psr = OptionParser(usage=usage)
    psr.add_option('--role', action='store', type='string', default=False, dest='role', help="server's role (manager/node)")
    psr.add_option('--public_if', action='store', type='string', default=False, dest='public_if', help="server's public network interface")
    psr.add_option('--private_if', action='store', type='string', default=False, dest='private_if', help="server's private network interface")
    psr.add_option('--private_network', action='store', type='string', default=False, dest='private_network', help="server's own network address")
    psr.add_option('--private_netmask', action='store', type='string', default=False, dest='private_netmask', help="server's own network netmask")
    psr.add_option('--log', action='store', type='string', default=False, dest='log', help='logfile path')
    psr.add_option('--loglevel', action='store', type='string', default=False, dest='loglevel', help='loglevel (DEBUG/INFO/WARING/ERROR/CRITICAL)')
#    psr.add_option('--action', action='store', type='string', dest='action', help='network management action (wipe/revive/show/add_network/del_network/add_ip/del_ip/add_nat/del_nat/set_filter/add_instance_bridge/del_instance_bridge/init_network)')
    psr.add_option('--action', action='store', type='string', dest='action', help='network management action (init/clear/show/add_network/del_network/add_ip/del_ip/add_nat/del_nat/set_filter/import)')
    psr.add_option('--vlan' , action='store', type='int', dest='vlan', help='VLAN ID')
    psr.add_option('--network', action='store', type='string', dest='network', help='network address for VLAN')
    psr.add_option('--netmask', action='store', type='string', dest='netmask', help='netmask for VLAN')
    psr.add_option('--gateway', action='store', type='string', dest='gateway', help='gateway address for VLAN')
    psr.add_option('--broadcast', action='store', type='string', dest='broadcast', help='broadcat address for VLAN')
    psr.add_option('--nameserver', action='store', type='string', dest='nameserver', help='nameserver address for VLAN')
    psr.add_option('--dhcp', action='store', type='string', dest='dhcp', help='dhcp address for VLAN')
    psr.add_option('--username', action='store', type='string', dest='username', help='user name of VLAN')
    psr.add_option('--ip' , action='store', type='string', dest='ip', help="instance's IP address")
    psr.add_option('--mac', action='store', type='string', dest='mac', help="instance's MAC address")
    psr.add_option('--publicip', action='store', type='string', dest='publicip', help='public IP address binding by NAT')
    psr.add_option('--privateip', action='store', type='string', dest='privateip', help='private IP address binding by NAT')
    psr.add_option('--bridge', action='store', type='string', dest='bridge', help='instance bridge prefix name')
    psr.add_option('--filtertype', action='store', type='string', dest='filtertype', help='netfilter filter action type (open/close)')
    psr.add_option('--destname', action='store', type='string', dest='destname', help='netfilter filter destination user name')
    psr.add_option('--sourcename', action='store', type='string',default=False, dest='sourcename', help='netfilter filter source user name')
    psr.add_option('--sourcenet', action='store', type='string', default=False, dest='sourcenet', help='netfilter filter source network')
    psr.add_option('--protocol', action='store', type='string', default=False, dest='protocol', help='netfilter filter protocol name')
    psr.add_option('--minport', action='store', type='string', default=False, dest='minport', help='netfilter filter port range min')
    psr.add_option('--maxport', action='store', type='string', default=False, dest='maxport', help='netfilter filter port range max')
    psr.add_option('--csv', action='store', type='string', default=False, dest='csv', help='import csv file path')
    psr.add_option('--nodump', action="store_true", dest="nodump", default=False, help='do not write db flag')

    (opts, args) = psr.parse_args(sys.argv)

    nwa = NwHandler(PATH_CONFIG, PATH_DATA_NW)

    if opts.action:
        if opts.action == 'import':
            if opts.csv:
                reader = csv.DictReader(file(opts.csv, "rb"))
                for network in reader:
                    if nwa.add_network(network["vlan"], network["address"], network["mask"], network["broadcast"], network["gateway"], network["dns"], network["dhcp"], network["name"], get_nodump(opts)):
                        print "%s is added." % network["name"]
                    else:
                        print "%s is faild!" % network["name"]
                        exit(1)
                print "init network: done."
            else:
                print "We need those options: --csv."
        elif opts.action == 'init':
            if nwa.init(False, get_nodump(opts)):
                print "init: done."
            else:
                print "init: failed!"
        elif opts.action == 'clear':
            if nwa.init(True, get_nodump(opts)):
                print "clear: done."
            else:
                print "clear: failed!"
#        elif opts.action == 'revive':
#            if nwa.revive():
#                print "revive: done."
#            else:
#                print "revive: failed!"
#                exit(1)
        elif opts.action == 'show':
            config_print(nwa.get_config())
        elif opts.action == 'add_network':
            if opts.vlan and opts.network and opts.netmask and opts.broadcast and opts.gateway and opts.nameserver and opts.username:
                if nwa.add_network(opts.vlan, opts.network, opts.netmask, opts.broadcast, opts.gateway, opts.nameserver, opts.username, get_nodump(opts)):
                    print "add network: done."
                else:
                    print "add network: failed!"
            else:
                print "We need those options: --vlan, --network, --netmask,--broadcast, --gateway, --nameserver, --dhcp, --username."
                exit(1)
        elif opts.action == 'del_network':
            if opts.vlan:
                if nwa.del_network(opts.vlan, get_nodump(opts)):
                    print "del network: done."
                else:
                    print "del network: failed!"
            else:
                print "We need those options: --vlan."
                exit(1)
        elif opts.action == 'add_ip':
            if opts.ip and opts.mac:
                if nwa.add_ip(opts.ip, opts.mac, get_nodump(opts)):
                    print "add ip: done."
                else:
                    print "add ip: failed!"
            else:
                print "We need those options: --ip, --mac."
                exit(1)
        elif opts.action == 'del_ip':
            if opts.ip and opts.mac:
                if nwa.del_ip(opts.ip, opts.mac, get_nodump(opts)):
                    print "del ip: done."
                else:
                    print "del ip: failed!"
            else:
                print "We need those options: --ip, --mac."
        elif opts.action == 'add_nat':
            if opts.publicip and opts.privateip:
                if nwa.add_nat(opts.privateip, opts.publicip, get_nodump(opts)):
                    print "add nat: done."
                else:
                    print "add nat: failed!"
            else:
                print "We need those options: --publicip, --privateip."
        elif opts.action == 'del_nat':
            if opts.publicip and opts.privateip:
                if nwa.del_nat(opts.privateip, opts.publicip, get_nodump(opts)):
                    print "del nat: done."
                else:
                    print "del nat: failed!"
                    exit(1)
            else:
                print "We need those options: --publicip, --privateip."
        elif opts.action == 'set_filter':
            if opts.filtertype and opts.destname and (opts.sourcename or opts.sourcenet):
                if nwa.set_filter(opts.filtertype, opts.destname, other_username=opts.sourcename,
                                   other_net=opts.sourcenet, protocol=opts.protocol, minport=opts.minport, maxport=opts.maxport, nodump=get_nodump(opts)):
                    print "set filter: done."
                else:
                    print "set filter: failed!"
            else:
                print "We need those options: --filtertype, --destname, --sourcename or --sourcenet."
#        elif opts.action == 'add_instance_bridge':
#            if opts.vlan and opts.bridge:
#                if dvn.add_instance_bridge(opts.vlan, opts.bridge):
#                    print "add instance bridge: done."
#                else:
#                    print "add instance bridge: failed!"
#                    exit(1)
#            else:
#                print "We need those options: --vlan, --bridge."
#                exit(1)
#        elif opts.action == 'del_instance_bridge':
#            if opts.vlan and opts.bridge:
#                if dvn.del_instance_bridge(opts.vlan, opts.bridge):
#                    print "add instance bridge: done."
#                else:
#                    print "add instance bridge: failed!"
#                    exit(1)
#            else:
#                print "We need those options: --vlan, --bridge."
#                exit(1)
#        else:
#            psr.print_help()
    else:
        print "We need at least this option: --action."
        print "\n"
        psr.print_help()

def config_print(conf):
    print '================= Configuration ================='
#        print "role: %s" % conf.role
    print "public IF: %s" % conf.public_if
    print "private IF: %s" % conf.private_if
    print "server's network: %s/%s" % (conf.private_network, conf.private_netmask)
    for i in conf.networks.keys():
        print '-------------'
        print "VLAN ID: %d" % i
        print "network: %s" % conf.networks[i]['network']
        print "netmask: %s" % conf.networks[i]['netmask']
        print "broadcast: %s" % conf.networks[i]['broadcast']
        print "gateway: %s" % conf.networks[i]['gateway']
        print "nameserver: %s" % conf.networks[i]['nameserver']
        print "username: %s" % conf.networks[i]['username']
    print '-------------'
    print "publicips: %s" % conf.publicips
    print "addresses: %s" % conf.addresses
    print 'netfilter: start->'
    print "%s" % conf.netfilter
    print 'netfilter: <-end'
    print '================================================='
    return True

def get_nodump(opts):
    if opts.nodump:
        return 1
    else:
        return

if (__name__ == "__main__"):

    logging.config.fileConfig(os.environ[KEY_ENV_VHUT_HOME]+"/agent/conf/log.conf")

    if os.getuid() == 0:
        main()
#        exit()
    else:
        print "You must be root."
        exit(1)

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
