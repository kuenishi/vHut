/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ApplicationInstanceGroupNames._ApplicationInstanceGroupNames;
import jp.co.ntts.vhut.entity.ApplicationInstanceNames._ApplicationInstanceNames;
import jp.co.ntts.vhut.entity.ApplicationInstanceSecurityGroupNames._ApplicationInstanceSecurityGroupNames;
import jp.co.ntts.vhut.entity.ApplicationInstanceVmNames._ApplicationInstanceVmNames;
import jp.co.ntts.vhut.entity.ApplicationInstanceVmSecurityGroupMapNames._ApplicationInstanceVmSecurityGroupMapNames;
import jp.co.ntts.vhut.entity.ApplicationNames._ApplicationNames;
import jp.co.ntts.vhut.entity.ApplicationSecurityGroupNames._ApplicationSecurityGroupNames;
import jp.co.ntts.vhut.entity.ApplicationVmNames._ApplicationVmNames;
import jp.co.ntts.vhut.entity.ApplicationVmSecurityGroupMapNames._ApplicationVmSecurityGroupMapNames;
import jp.co.ntts.vhut.entity.BaseTemplateNames._BaseTemplateNames;
import jp.co.ntts.vhut.entity.CloudNames._CloudNames;
import jp.co.ntts.vhut.entity.CloudUserNames._CloudUserNames;
import jp.co.ntts.vhut.entity.ClusterNames._ClusterNames;
import jp.co.ntts.vhut.entity.ClusterReservationNames._ClusterReservationNames;
import jp.co.ntts.vhut.entity.ClusterReservationVmMapNames._ClusterReservationVmMapNames;
import jp.co.ntts.vhut.entity.ClusterResourceNames._ClusterResourceNames;
import jp.co.ntts.vhut.entity.CommandNames._CommandNames;
import jp.co.ntts.vhut.entity.CommandTemplateMapNames._CommandTemplateMapNames;
import jp.co.ntts.vhut.entity.CommandVmMapNames._CommandVmMapNames;
import jp.co.ntts.vhut.entity.ConflictNames._ConflictNames;
import jp.co.ntts.vhut.entity.DiskNames._DiskNames;
import jp.co.ntts.vhut.entity.DiskTemplateNames._DiskTemplateNames;
import jp.co.ntts.vhut.entity.HostNames._HostNames;
import jp.co.ntts.vhut.entity.LocalIdNames._LocalIdNames;
import jp.co.ntts.vhut.entity.NetworkAdapterNames._NetworkAdapterNames;
import jp.co.ntts.vhut.entity.NetworkAdapterTemplateNames._NetworkAdapterTemplateNames;
import jp.co.ntts.vhut.entity.NetworkNames._NetworkNames;
import jp.co.ntts.vhut.entity.PublicIpReservationNames._PublicIpReservationNames;
import jp.co.ntts.vhut.entity.PublicIpResourceNames._PublicIpResourceNames;
import jp.co.ntts.vhut.entity.ReleasedApplicationNames._ReleasedApplicationNames;
import jp.co.ntts.vhut.entity.ReleasedApplicationSecurityGroupTemplateNames._ReleasedApplicationSecurityGroupTemplateNames;
import jp.co.ntts.vhut.entity.ReleasedApplicationTemplateNames._ReleasedApplicationTemplateNames;
import jp.co.ntts.vhut.entity.ReleasedApplicationTemplateSecurityGroupMapNames._ReleasedApplicationTemplateSecurityGroupMapNames;
import jp.co.ntts.vhut.entity.ReservationNames._ReservationNames;
import jp.co.ntts.vhut.entity.RoleNames._RoleNames;
import jp.co.ntts.vhut.entity.SecurityGroupNames._SecurityGroupNames;
import jp.co.ntts.vhut.entity.SecurityGroupTemplateNames._SecurityGroupTemplateNames;
import jp.co.ntts.vhut.entity.StorageNames._StorageNames;
import jp.co.ntts.vhut.entity.StorageReservationDiskMapNames._StorageReservationDiskMapNames;
import jp.co.ntts.vhut.entity.StorageReservationDiskTemplateMapNames._StorageReservationDiskTemplateMapNames;
import jp.co.ntts.vhut.entity.StorageReservationNames._StorageReservationNames;
import jp.co.ntts.vhut.entity.StorageResourceNames._StorageResourceNames;
import jp.co.ntts.vhut.entity.TemplateNames._TemplateNames;
import jp.co.ntts.vhut.entity.TermNames._TermNames;
import jp.co.ntts.vhut.entity.VhutUserCloudUserMapNames._VhutUserCloudUserMapNames;
import jp.co.ntts.vhut.entity.VhutUserNames._VhutUserNames;
import jp.co.ntts.vhut.entity.VhutUserRoleMapNames._VhutUserRoleMapNames;
import jp.co.ntts.vhut.entity.VlanReservationNames._VlanReservationNames;
import jp.co.ntts.vhut.entity.VlanResourceNames._VlanResourceNames;
import jp.co.ntts.vhut.entity.VmCloudUserMapNames._VmCloudUserMapNames;
import jp.co.ntts.vhut.entity.VmNames._VmNames;

/**
 * 名前クラスの集約です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesAggregateModelFactoryImpl"}, date = "2011/07/15 22:08:02")
public class Names {

    /**
     * {@link Application}の名前クラスを返します。
     * 
     * @return Applicationの名前クラス
     */
    public static _ApplicationNames application() {
        return new _ApplicationNames();
    }

    /**
     * {@link ApplicationInstance}の名前クラスを返します。
     * 
     * @return ApplicationInstanceの名前クラス
     */
    public static _ApplicationInstanceNames applicationInstance() {
        return new _ApplicationInstanceNames();
    }

    /**
     * {@link ApplicationInstanceGroup}の名前クラスを返します。
     * 
     * @return ApplicationInstanceGroupの名前クラス
     */
    public static _ApplicationInstanceGroupNames applicationInstanceGroup() {
        return new _ApplicationInstanceGroupNames();
    }

    /**
     * {@link ApplicationInstanceSecurityGroup}の名前クラスを返します。
     * 
     * @return ApplicationInstanceSecurityGroupの名前クラス
     */
    public static _ApplicationInstanceSecurityGroupNames applicationInstanceSecurityGroup() {
        return new _ApplicationInstanceSecurityGroupNames();
    }

    /**
     * {@link ApplicationInstanceVm}の名前クラスを返します。
     * 
     * @return ApplicationInstanceVmの名前クラス
     */
    public static _ApplicationInstanceVmNames applicationInstanceVm() {
        return new _ApplicationInstanceVmNames();
    }

    /**
     * {@link ApplicationInstanceVmSecurityGroupMap}の名前クラスを返します。
     * 
     * @return ApplicationInstanceVmSecurityGroupMapの名前クラス
     */
    public static _ApplicationInstanceVmSecurityGroupMapNames applicationInstanceVmSecurityGroupMap() {
        return new _ApplicationInstanceVmSecurityGroupMapNames();
    }

    /**
     * {@link ApplicationSecurityGroup}の名前クラスを返します。
     * 
     * @return ApplicationSecurityGroupの名前クラス
     */
    public static _ApplicationSecurityGroupNames applicationSecurityGroup() {
        return new _ApplicationSecurityGroupNames();
    }

    /**
     * {@link ApplicationVm}の名前クラスを返します。
     * 
     * @return ApplicationVmの名前クラス
     */
    public static _ApplicationVmNames applicationVm() {
        return new _ApplicationVmNames();
    }

    /**
     * {@link ApplicationVmSecurityGroupMap}の名前クラスを返します。
     * 
     * @return ApplicationVmSecurityGroupMapの名前クラス
     */
    public static _ApplicationVmSecurityGroupMapNames applicationVmSecurityGroupMap() {
        return new _ApplicationVmSecurityGroupMapNames();
    }

    /**
     * {@link BaseTemplate}の名前クラスを返します。
     * 
     * @return BaseTemplateの名前クラス
     */
    public static _BaseTemplateNames baseTemplate() {
        return new _BaseTemplateNames();
    }

    /**
     * {@link Cloud}の名前クラスを返します。
     * 
     * @return Cloudの名前クラス
     */
    public static _CloudNames cloud() {
        return new _CloudNames();
    }

    /**
     * {@link CloudUser}の名前クラスを返します。
     * 
     * @return CloudUserの名前クラス
     */
    public static _CloudUserNames cloudUser() {
        return new _CloudUserNames();
    }

    /**
     * {@link Cluster}の名前クラスを返します。
     * 
     * @return Clusterの名前クラス
     */
    public static _ClusterNames cluster() {
        return new _ClusterNames();
    }

    /**
     * {@link ClusterReservation}の名前クラスを返します。
     * 
     * @return ClusterReservationの名前クラス
     */
    public static _ClusterReservationNames clusterReservation() {
        return new _ClusterReservationNames();
    }

    /**
     * {@link ClusterReservationVmMap}の名前クラスを返します。
     * 
     * @return ClusterReservationVmMapの名前クラス
     */
    public static _ClusterReservationVmMapNames clusterReservationVmMap() {
        return new _ClusterReservationVmMapNames();
    }

    /**
     * {@link ClusterResource}の名前クラスを返します。
     * 
     * @return ClusterResourceの名前クラス
     */
    public static _ClusterResourceNames clusterResource() {
        return new _ClusterResourceNames();
    }

    /**
     * {@link Command}の名前クラスを返します。
     * 
     * @return Commandの名前クラス
     */
    public static _CommandNames command() {
        return new _CommandNames();
    }

    /**
     * {@link CommandTemplateMap}の名前クラスを返します。
     * 
     * @return CommandTemplateMapの名前クラス
     */
    public static _CommandTemplateMapNames commandTemplateMap() {
        return new _CommandTemplateMapNames();
    }

    /**
     * {@link CommandVmMap}の名前クラスを返します。
     * 
     * @return CommandVmMapの名前クラス
     */
    public static _CommandVmMapNames commandVmMap() {
        return new _CommandVmMapNames();
    }

    /**
     * {@link Conflict}の名前クラスを返します。
     * 
     * @return Conflictの名前クラス
     */
    public static _ConflictNames conflict() {
        return new _ConflictNames();
    }

    /**
     * {@link Disk}の名前クラスを返します。
     * 
     * @return Diskの名前クラス
     */
    public static _DiskNames disk() {
        return new _DiskNames();
    }

    /**
     * {@link DiskTemplate}の名前クラスを返します。
     * 
     * @return DiskTemplateの名前クラス
     */
    public static _DiskTemplateNames diskTemplate() {
        return new _DiskTemplateNames();
    }

    /**
     * {@link Host}の名前クラスを返します。
     * 
     * @return Hostの名前クラス
     */
    public static _HostNames host() {
        return new _HostNames();
    }

    /**
     * {@link LocalId}の名前クラスを返します。
     * 
     * @return LocalIdの名前クラス
     */
    public static _LocalIdNames localId() {
        return new _LocalIdNames();
    }

    /**
     * {@link Network}の名前クラスを返します。
     * 
     * @return Networkの名前クラス
     */
    public static _NetworkNames network() {
        return new _NetworkNames();
    }

    /**
     * {@link NetworkAdapter}の名前クラスを返します。
     * 
     * @return NetworkAdapterの名前クラス
     */
    public static _NetworkAdapterNames networkAdapter() {
        return new _NetworkAdapterNames();
    }

    /**
     * {@link NetworkAdapterTemplate}の名前クラスを返します。
     * 
     * @return NetworkAdapterTemplateの名前クラス
     */
    public static _NetworkAdapterTemplateNames networkAdapterTemplate() {
        return new _NetworkAdapterTemplateNames();
    }

    /**
     * {@link PublicIpReservation}の名前クラスを返します。
     * 
     * @return PublicIpReservationの名前クラス
     */
    public static _PublicIpReservationNames publicIpReservation() {
        return new _PublicIpReservationNames();
    }

    /**
     * {@link PublicIpResource}の名前クラスを返します。
     * 
     * @return PublicIpResourceの名前クラス
     */
    public static _PublicIpResourceNames publicIpResource() {
        return new _PublicIpResourceNames();
    }

    /**
     * {@link ReleasedApplication}の名前クラスを返します。
     * 
     * @return ReleasedApplicationの名前クラス
     */
    public static _ReleasedApplicationNames releasedApplication() {
        return new _ReleasedApplicationNames();
    }

    /**
     * {@link ReleasedApplicationSecurityGroupTemplate}の名前クラスを返します。
     * 
     * @return ReleasedApplicationSecurityGroupTemplateの名前クラス
     */
    public static _ReleasedApplicationSecurityGroupTemplateNames releasedApplicationSecurityGroupTemplate() {
        return new _ReleasedApplicationSecurityGroupTemplateNames();
    }

    /**
     * {@link ReleasedApplicationTemplate}の名前クラスを返します。
     * 
     * @return ReleasedApplicationTemplateの名前クラス
     */
    public static _ReleasedApplicationTemplateNames releasedApplicationTemplate() {
        return new _ReleasedApplicationTemplateNames();
    }

    /**
     * {@link ReleasedApplicationTemplateSecurityGroupMap}の名前クラスを返します。
     * 
     * @return ReleasedApplicationTemplateSecurityGroupMapの名前クラス
     */
    public static _ReleasedApplicationTemplateSecurityGroupMapNames releasedApplicationTemplateSecurityGroupMap() {
        return new _ReleasedApplicationTemplateSecurityGroupMapNames();
    }

    /**
     * {@link Reservation}の名前クラスを返します。
     * 
     * @return Reservationの名前クラス
     */
    public static _ReservationNames reservation() {
        return new _ReservationNames();
    }

    /**
     * {@link Role}の名前クラスを返します。
     * 
     * @return Roleの名前クラス
     */
    public static _RoleNames role() {
        return new _RoleNames();
    }

    /**
     * {@link SecurityGroup}の名前クラスを返します。
     * 
     * @return SecurityGroupの名前クラス
     */
    public static _SecurityGroupNames securityGroup() {
        return new _SecurityGroupNames();
    }

    /**
     * {@link SecurityGroupTemplate}の名前クラスを返します。
     * 
     * @return SecurityGroupTemplateの名前クラス
     */
    public static _SecurityGroupTemplateNames securityGroupTemplate() {
        return new _SecurityGroupTemplateNames();
    }

    /**
     * {@link Storage}の名前クラスを返します。
     * 
     * @return Storageの名前クラス
     */
    public static _StorageNames storage() {
        return new _StorageNames();
    }

    /**
     * {@link StorageReservation}の名前クラスを返します。
     * 
     * @return StorageReservationの名前クラス
     */
    public static _StorageReservationNames storageReservation() {
        return new _StorageReservationNames();
    }

    /**
     * {@link StorageReservationDiskMap}の名前クラスを返します。
     * 
     * @return StorageReservationDiskMapの名前クラス
     */
    public static _StorageReservationDiskMapNames storageReservationDiskMap() {
        return new _StorageReservationDiskMapNames();
    }

    /**
     * {@link StorageReservationDiskTemplateMap}の名前クラスを返します。
     * 
     * @return StorageReservationDiskTemplateMapの名前クラス
     */
    public static _StorageReservationDiskTemplateMapNames storageReservationDiskTemplateMap() {
        return new _StorageReservationDiskTemplateMapNames();
    }

    /**
     * {@link StorageResource}の名前クラスを返します。
     * 
     * @return StorageResourceの名前クラス
     */
    public static _StorageResourceNames storageResource() {
        return new _StorageResourceNames();
    }

    /**
     * {@link Template}の名前クラスを返します。
     * 
     * @return Templateの名前クラス
     */
    public static _TemplateNames template() {
        return new _TemplateNames();
    }

    /**
     * {@link Term}の名前クラスを返します。
     * 
     * @return Termの名前クラス
     */
    public static _TermNames term() {
        return new _TermNames();
    }

    /**
     * {@link VhutUser}の名前クラスを返します。
     * 
     * @return VhutUserの名前クラス
     */
    public static _VhutUserNames vhutUser() {
        return new _VhutUserNames();
    }

    /**
     * {@link VhutUserCloudUserMap}の名前クラスを返します。
     * 
     * @return VhutUserCloudUserMapの名前クラス
     */
    public static _VhutUserCloudUserMapNames vhutUserCloudUserMap() {
        return new _VhutUserCloudUserMapNames();
    }

    /**
     * {@link VhutUserRoleMap}の名前クラスを返します。
     * 
     * @return VhutUserRoleMapの名前クラス
     */
    public static _VhutUserRoleMapNames vhutUserRoleMap() {
        return new _VhutUserRoleMapNames();
    }

    /**
     * {@link VlanReservation}の名前クラスを返します。
     * 
     * @return VlanReservationの名前クラス
     */
    public static _VlanReservationNames vlanReservation() {
        return new _VlanReservationNames();
    }

    /**
     * {@link VlanResource}の名前クラスを返します。
     * 
     * @return VlanResourceの名前クラス
     */
    public static _VlanResourceNames vlanResource() {
        return new _VlanResourceNames();
    }

    /**
     * {@link Vm}の名前クラスを返します。
     * 
     * @return Vmの名前クラス
     */
    public static _VmNames vm() {
        return new _VmNames();
    }

    /**
     * {@link VmCloudUserMap}の名前クラスを返します。
     * 
     * @return VmCloudUserMapの名前クラス
     */
    public static _VmCloudUserMapNames vmCloudUserMap() {
        return new _VmCloudUserMapNames();
    }
}

/**
 * =====================================================================
 * 
 *    Copyright 2011 NTT Sofware Corporation
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * =====================================================================
 */
