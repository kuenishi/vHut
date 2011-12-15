/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.logic;

/*HeaderTest*/
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import jp.co.ntts.vhut.dto.AdditionalDiskDto;
import jp.co.ntts.vhut.dto.OrderDto;
import jp.co.ntts.vhut.dto.RealmDto;
import jp.co.ntts.vhut.dto.ResourceDto;
import jp.co.ntts.vhut.dto.SpecDto;
import jp.co.ntts.vhut.dto.SwitchTemplateDto;
import jp.co.ntts.vhut.entity.CloudUser;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.entity.ExternalIpRequestMode;
import jp.co.ntts.vhut.entity.Network;
import jp.co.ntts.vhut.entity.NetworkAdapter;
import jp.co.ntts.vhut.entity.NetworkAdapterTemplate;
import jp.co.ntts.vhut.entity.Reservation;
import jp.co.ntts.vhut.entity.SecurityGroup;
import jp.co.ntts.vhut.entity.SecurityGroupTemplate;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.Term;
import jp.co.ntts.vhut.entity.Vm;

/**
 * パブリッククラウドを操作して仮想マシンの管理を行います.
 * 
 * @version 1.0.0
 * @author NTT Software Corporation.
 * 
 *         <!-- $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $ $Revision: 913
 *         $ $Author: NTT Software Corporation. $ -->
 */
public class PublicCloudLogic implements ICloudServiceLogic {

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#addReservation(long,
	 * jp.co.ntts.vhut.dto.OrderDto)
	 */
	@Override
	public Reservation addReservation(long reservationID, OrderDto order) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#batchUpdateVmUser(long,
	 * long)
	 */
	@Override
	public List<Vm> batchUpdateVmUser(long oldUserId, long newUserId) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#cancelCommand(long)
	 */
	@Override
	public Command cancelCommand(long commandId) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jp.co.ntts.vhut.logic.ICloudServiceLogic#createReservation(jp.co.ntts
	 * .vhut.dto.OrderDto)
	 */
	@Override
	public Reservation createReservation(OrderDto order) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#createTemplate(long, long,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public Template createTemplate(long reservationId, long vmId, String name,
			String description) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteReservation(long)
	 */
	@Override
	public void deleteReservation(long reservationID) {
		// NEXT Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteVm(long)
	 */
	@Override
	public void deleteVm(long vmId) {
		// NEXT Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllDiskTemplateList()
	 */
	@Override
	public List<AdditionalDiskDto> getAllAdditionalDiskList() {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllNetworkList()
	 */
	@Override
	public List<Network> getAllNetworkList() {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllRealmList()
	 */
	@Override
	public List<RealmDto> getAllRealmList() {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllSpecList()
	 */
	@Override
	public List<SpecDto> getAllSpecList() {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllSwitchTemplateList()
	 */
	@Override
	public List<SwitchTemplateDto> getAllSwitchTemplateList() {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllTemplateList()
	 */
	@Override
	public List<Template> getAllTemplateList() {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllUsers()
	 */
	@Override
	public List<CloudUser> getAllUserList() {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllVmList()
	 */
	@Override
	public List<Vm> getAllVmList() {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getCommandAbstractionList()
	 */
	@Override
	public List<Command> getCommandAbstractionList() {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jp.co.ntts.vhut.logic.ICloudServiceLogic#getCommandListByTemplateId(long)
	 */
	@Override
	public List<Command> getCommandListByTemplateId(long templateId) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getCommandListByVmId(long)
	 */
	@Override
	public List<Command> getCommandListByVmId(long vmId) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jp.co.ntts.vhut.logic.ICloudServiceLogic#getPrediction(java.util.Date,
	 * java.util.Date)
	 */
	// @Override
	// public PredictionDto getPrediction(Date startTime, Date endTime) {
	// // NEXT Auto-generated method stub
	// return null;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jp.co.ntts.vhut.logic.ICloudServiceLogic#getResourceListByTerm(java.sql
	 * .Time, java.sql.Time)
	 */
	@Override
	public List<ResourceDto> getResourceListByTerm(Timestamp startTime,
			Timestamp endTime) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getTemplateById(long)
	 */
	@Override
	public Template getTemplateById(long templateId) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getVmById(long)
	 */
	@Override
	public Vm getVmById(long vmId) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#rebuildVm(long)
	 */
	@Override
	public void rebuildVm(long vmId) {
		// NEXT Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#removeVmUser(long, long)
	 */
	@Override
	public Vm removeVmUser(long vmId, long userId) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#retryCommand(long)
	 */
	@Override
	public Command retryCommand(long commandId) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#shutdownVm(long)
	 */
	@Override
	public Vm shutdownVm(long vmId) {
		// NEXT Auto-generated method stub
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#startVm(long)
	 */
	@Override
	public Vm startVm(long vmId) {
		// NEXT Auto-generated method stub
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#stopVm(long)
	 */
	@Override
	public Vm stopVm(long vmId) {
		// NEXT Auto-generated method stub
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#updateReservation(long,
	 * jp.co.ntts.vhut.dto.OrderDto)
	 */
	@Override
	public Reservation updateReservation(long reservationID, OrderDto order) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#updateVm(long,
	 * jp.co.ntts.vhut.entity.Vm)
	 */
	@Override
	public Vm updateVm(long reservationId, Vm vm) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#setCloudId(long)
	 */
	@Override
	public void setCloudId(long cloudId) {
		// NEXT Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getTermListToStartVm(long,
	 * java.util.Date, java.util.Date)
	 */
	@Override
	public List<Term> getTermListToReserve(OrderDto order) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteTemplate(long)
	 */
	@Override
	public void deleteTemplate(long templateId) {
		// NEXT Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#createSecurityGroup()
	 */
	@Override
	public SecurityGroup createSecurityGroup() {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteSecurityGroup(long)
	 */
	@Override
	public void deleteSecurityGroup(long securityGroupId) {
		// NEXT Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#createNetworkAdapter(long,
	 * long)
	 */
	@Override
	public NetworkAdapter createNetworkAdapter(long vmId, long securityGroupId) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jp.co.ntts.vhut.logic.ICloudServiceLogic#createNetworkAdapterTemplate
	 * (long, long)
	 */
	@Override
	public NetworkAdapterTemplate createNetworkAdapterTemplate(long templateId,
			long securityGroupTemplateId) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jp.co.ntts.vhut.logic.ICloudServiceLogic#createSecurityGroupTemplate()
	 */
	@Override
	public SecurityGroupTemplate createSecurityGroupTemplate() {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteSecurityGroupTemplate(
	 * long)
	 */
	@Override
	public void deleteSecurityGroupTemplate(long securityGroupTemplateId) {
		// NEXT Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#addVmUser(long, long, long)
	 */
	@Override
	public Vm addVmUser(long reservationId, long vmId, long cloudUserId) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jp.co.ntts.vhut.logic.ICloudServiceLogic#createReservationList(java.util
	 * .List)
	 */
	@Override
	public List<Reservation> createReservationList(List<OrderDto> orderDtoList) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#createVm(long, long, long,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public Vm createVm(long reservationId, long templateId, long specId,
			String name, String description) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteNetworkAdapter(long,
	 * long)
	 */
	@Override
	public void deleteNetworkAdapter(long vmId, long securityGroupId) {
		// NEXT Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteNetworkAdapterTemplate
	 * (long, java.lang.Long)
	 */
	@Override
	public void deleteNetworkAdapterTemplate(long templateId,
			Long securityGroupTemplateId) {
		// NEXT Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#obtainNetwork(long, long)
	 */
	@Override
	public Network obtainNetwork(long reservationId, long securityGroupId,
			ExternalIpRequestMode exIpRequestMode) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#releaseNetwork(long)
	 */
	@Override
	public void releaseNetwork(long securityGroupId) {
		// NEXT Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getSecurityGroupById(long)
	 */
	@Override
	public SecurityGroup getSecurityGroupById(long id) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jp.co.ntts.vhut.logic.ICloudServiceLogic#getSecurityGroupTemplateById
	 * (long)
	 */
	@Override
	public SecurityGroupTemplate getSecurityGroupTemplateById(long id) {
		// NEXT Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jp.co.ntts.vhut.logic.ICloudServiceLogic#searchCommandList(java.lang.
	 * String, java.sql.Timestamp, java.sql.Timestamp)
	 */

	public List<Command> searchCommandList(String keyword, Timestamp startDate,
			Timestamp endDate, Collection<CommandOperation> operations,
			Collection<CommandStatus> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jp.co.ntts.vhut.logic.ICloudServiceLogic#searchCommandListByTemplateIds
	 * (java.util.List, java.sql.Timestamp, java.sql.Timestamp)
	 */

	public List<Command> searchCommandListByTemplateIds(
			Collection<Long> templateIds, Timestamp startDate,
			Timestamp endDate, Collection<CommandOperation> operations,
			Collection<CommandStatus> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jp.co.ntts.vhut.logic.ICloudServiceLogic#searchCommandListByVmIds(java
	 * .util.List, java.sql.Timestamp, java.sql.Timestamp)
	 */
	public List<Command> searchCommandListByVmIds(Collection<Long> vmIds,
			Timestamp startDate, Timestamp endDate,
			Collection<CommandOperation> operations,
			Collection<CommandStatus> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String changeUsersPassword(List<String> accountList) {
		return null;
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
