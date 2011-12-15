/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.util;

import java.sql.Timestamp;
import java.util.ArrayList;

import jp.co.ntts.vhut.dto.OrderDto;
import jp.co.ntts.vhut.dto.OrderItem;
import jp.co.ntts.vhut.entity.Application;
import jp.co.ntts.vhut.entity.ApplicationInstance;
import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
import jp.co.ntts.vhut.entity.ApplicationInstanceVm;
import jp.co.ntts.vhut.entity.ApplicationSecurityGroup;
import jp.co.ntts.vhut.entity.ApplicationVm;
import jp.co.ntts.vhut.entity.ApplicationVmSecurityGroupMap;
import jp.co.ntts.vhut.entity.Disk;
import jp.co.ntts.vhut.entity.ExternalIpRequestMode;
import jp.co.ntts.vhut.entity.Network;
import jp.co.ntts.vhut.entity.ReleasedApplication;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.exception.NotJoinedRuntimeException;

import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.hotdeploy.HotdeployUtil;

/**
 * <p>各種データからリソースの予約に必要なOrderDtoを作成します.
 * <br>
 * <p>OrderDtoはCloudLogicに実装させれている予約系のメソッドを活用する際に使用します.
 *
 * @version 1.0.0
 * @author NTT Software Corporation.
 *
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
public class OrderUtil {

    private OrderUtil() {

    }

    /**
     * <p>{@link ApplicationInstanceGroup} に所属する
     * {@link ApplicationInstance}が所有する
     * {@link ApplicationInstanceVm}の起動に必要なリソースに対する要求条件を作成します.
     * <br>
     * 作成した要求条件はCloudLogicに依頼して予約番号に変換します.
     *
     * @param aig 作成予定の{@link ApplicationInstanceGroup}
     * @param exIpRequestMode 外部IPの申請モード
     * @return 要求条件
     */
    public static OrderDto createOrderToStart(ApplicationInstanceGroup aig, ExternalIpRequestMode exIpRequestMode) {
        HotdeployUtil.start();
        OrderDto orderDto = SingletonS2Container.getComponent(OrderDto.class);
        HotdeployUtil.stop();

        orderDto.startTime = aig.startTime;
        orderDto.endTime = aig.endTime;

        if (aig.application == null) {
            throw new NotJoinedRuntimeException(Application.class);
        }

        Application app = aig.application;
        if (aig.applicationInstanceList == null) {
            return orderDto;
            //            throw new NotJoinedRuntimeException(ApplicationInstance.class);
        }
        // ApplicationInstanceの数
        int unit = aig.applicationInstanceList.size();
        if (unit > 0) {

            if (app.applicationVmList == null) {
                throw new NotJoinedRuntimeException(ApplicationVm.class);
            }

            if (app.applicationSecurityGroupList == null) {
                throw new NotJoinedRuntimeException(ApplicationSecurityGroup.class);
            }

            orderDto.items = new ArrayList<OrderItem>();
            //                int ipCount = 0;

            // VM起動用の注文を作成
            for (ApplicationVm avm : app.applicationVmList) {
                if (avm.vm == null) {
                    throw new NotJoinedRuntimeException(Vm.class);
                }
                if (avm.applicationVmSecurityGroupMapList == null) {
                    throw new NotJoinedRuntimeException(ApplicationVmSecurityGroupMap.class);
                }
                orderDto.items.add(OrderItem.newStartVmOrderItem(avm.vm, unit));
                // VM起動用の外部IPの注文を作成
                switch (exIpRequestMode) {
                    case AUTO:
                        orderDto.items.add(OrderItem.newObtainPublicIpOrderItem(avm.vm, avm.applicationVmSecurityGroupMapList.size()));
                        break;
                    case PERVM:
                        orderDto.items.add(OrderItem.newObtainPublicIpOrderItem(avm.vm, 1));
                        break;
                    default:
                        break;
                }
            }

            // VM起動用のNetworkの注文を作成
            for (ApplicationSecurityGroup asg : app.applicationSecurityGroupList) {
                if (asg.securityGroup == null) {
                    throw new NotJoinedRuntimeException(Network.class);
                }
                if (asg.applicationVmSecurityGroupMapList == null) {
                    throw new NotJoinedRuntimeException(Network.class);
                }
                orderDto.items.add(OrderItem.newObtainNetworkOrderItem(asg.securityGroup, unit));
                //                    ipCount += asg.applicationVmSecurityGroupMapList.size();
            }
            //　VM起動用の注文を作成のfor文内に移動・vｍを引数に追加
            //                // VM起動用の外部IPの注文を作成
            //                orderDto.items.add(OrderItem.newObtainPublicIpOrderItem(ipCount * unit));

        }
        return orderDto;
    }

    /**
     * <p>{@link ApplicationInstanceGroup} に所属する
     * {@link ApplicationInstance}が所有する
     * {@link ApplicationInstanceVm}の作成に必要なリソースに対する要求条件を作成します.
     * <br>
     * 作成した要求条件はCloudLogicに依頼して予約番号に変換します.
     *
     * @param aig 作成予定の{@link ApplicationInstanceGroup}
     * @return 要求条件
     */
    //    public static OrderDto createOrderToCreate(ApplicationInstanceGroup aig) {
    //        HotdeployUtil.start();
    //        OrderDto orderDto = SingletonS2Container.getComponent(OrderDto.class);
    //        HotdeployUtil.stop();
    //
    //        orderDto.startTime = aig.startTime;
    //        orderDto.endTime = aig.deleteTime;
    //
    //        if (aig.application != null) {
    //            Application application = aig.application;
    //            if (aig.applicationInstanceList == null) {
    //                throw new NotJoinedRuntimeException(ApplicationInstance.class);
    //            }
    //            // ApplicationInstanceの数
    //            int unit = aig.applicationInstanceList.size();
    //            if (unit > 0) {
    //                if (application.applicationVmList == null) {
    //                    throw new NotJoinedRuntimeException(ApplicationVm.class);
    //                }
    //                // VM作成用の注文を作成
    //                orderDto.items = new ArrayList<OrderItem>();
    //                for (ApplicationVm avm : application.applicationVmList) {
    //                    if (avm.vm == null) {
    //                        throw new NotJoinedRuntimeException(Vm.class);
    //                    }
    //                    orderDto.items.add(OrderItem.newCreateVmOrderItem(avm.vm,
    //                                                                      unit));
    //                }
    //            }
    //        } else {
    //            throw new EmptyRuntimeException("application");
    //        }
    //        return orderDto;
    //
    //    }

    /**
     * <p>{@link ReleasedApplication}が所有する
     * {@link ReleasedApplicationTemplate}の作成に必要なリソースに対する要求条件を作成します.
     * <br>
     * 作成した要求条件はCloudLogicに依頼して予約番号に変換します.
     *
     * @param rap 作成予定の{@link ReleasedApplication}
     * @return 要求条件
     */
    //    public static final OrderDto createOrderToCreate(ReleasedApplication rap) {
    //        HotdeployUtil.start();
    //        OrderDto orderDto = SingletonS2Container.getComponent(OrderDto.class);
    //        HotdeployUtil.stop();
    //
    //        if (rap.releasedApplicationTemplateList == null) {
    //            throw new NotJoinedRuntimeException(ReleasedApplicationTemplate.class);
    //        }
    //        // テンプレート作成用の注文を作成
    //        orderDto.items = new ArrayList<OrderItem>();
    //        for (ReleasedApplicationTemplate rat : rap.releasedApplicationTemplateList) {
    //            if (rat.template == null) {
    //                throw new NotJoinedRuntimeException(Template.class);
    //            }
    //            orderDto.items.add(OrderItem.newCreateTemplateOrderItem(rat.template));
    //
    //        }
    //
    //        return orderDto;
    //
    //    }

    /**
     * <p>{@link Application}が所有する
     * {@link ApplicationVm}の作成に必要なリソースに対する要求条件を作成します.
     * <br>
     * 作成した要求条件はCloudLogicに依頼して予約番号に変換します.
     *
     * @param app 作成予定の{@link Application}
     * @return 要求条件
     */
    //    public static final OrderDto createOrderToCreate(Application app) {
    //        HotdeployUtil.start();
    //        OrderDto orderDto = SingletonS2Container.getComponent(OrderDto.class);
    //        HotdeployUtil.stop();
    //
    //        if (app.applicationVmList == null) {
    //            throw new NotJoinedRuntimeException(ApplicationVm.class);
    //        }
    //        // テンプレート作成用の注文を作成
    //        orderDto.items = new ArrayList<OrderItem>();
    //        for (ApplicationVm av : app.applicationVmList) {
    //            if (av.vm == null) {
    //                throw new NotJoinedRuntimeException(Vm.class);
    //            }
    //            orderDto.items.add(OrderItem.newCreateVmOrderItem(av.vm));
    //        }
    //
    //        return orderDto;
    //
    //    }

    /**
     * <p>{@link jp.co.ntts.vhut.entity.Appliation}が所有する
     * {@link jp.co.ntts.vhut.entity.AppliationVm}の作成に必要なリソースに対する要求条件を作成します.
     * <br>
     * 下記が前提となります.
     * <ul>
     * <li>ApplicationVmにvmが設定されている.
     * <li>vmにdiskListが設定されている.
     * <li>vmにtemplateが設定されている.
     * <li>templateにdiskTemplateListが設定されている.
     * </ul>
     * 作成した要求条件はCloudLogicに依頼して予約番号に変換します.
     *
     * @param app 作成予定の{@link jp.co.ntts.vhut.entity.Appliation}
     * @return 要求条件
     */
    public static final OrderDto createOrderToCreateApplication(Application app) {
        HotdeployUtil.start();
        OrderDto orderDto = SingletonS2Container.getComponent(OrderDto.class);
        HotdeployUtil.stop();

        if (app.applicationVmList == null) {
            throw new NotJoinedRuntimeException(ApplicationVm.class);
        }
        // Vm作成用の注文を作成
        orderDto.items = new ArrayList<OrderItem>();
        for (ApplicationVm av : app.applicationVmList) {
            if (av.vm == null) {
                throw new NotJoinedRuntimeException(Vm.class);
            }
            if (av.vm.diskList == null) {
                throw new NotJoinedRuntimeException(Disk.class);
            }
            if (av.vm.template == null) {
                throw new NotJoinedRuntimeException(Template.class);
            }
            if (av.vm.template.diskTemplateList == null) {
                throw new NotJoinedRuntimeException(Template.class);
            }
            orderDto.items.add(OrderItem.newCreateVmOrderItem(av.vm));
        }

        return orderDto;
    }

    /**
     * <p>{@link jp.co.ntts.vhut.entity.Appliation}が所有する
     * {@link jp.co.ntts.vhut.entity.AppliationVm}の作成を参照して
     * {@link jp.co.ntts.vhut.entity.ReleasedAppliation}を作成するのに必要なリソースに対する要求条件を作成します.
     * <br>
     * 下記が前提となります.
     * <ul>
     * <li>ApplicationVmにvmが設定されている.
     * <li>vmにdiskListが設定されている.
     * </ul>
     * 作成した要求条件はCloudLogicに依頼して予約番号に変換します.
     *
     * @param aap 作成予定の{@link ReleasedApplication}
     * @return 要求条件
     */
    public static final OrderDto createOrderToCreateReleasedApplication(Application app) {
        HotdeployUtil.start();
        OrderDto orderDto = SingletonS2Container.getComponent(OrderDto.class);
        HotdeployUtil.stop();

        if (app.applicationVmList == null) {
            throw new NotJoinedRuntimeException(ApplicationVm.class);
        }
        // テンプレート作成用の注文を作成
        orderDto.items = new ArrayList<OrderItem>();
        for (ApplicationVm av : app.applicationVmList) {
            if (av.vm == null) {
                throw new NotJoinedRuntimeException(Vm.class);
            }
            if (av.vm.diskList == null) {
                throw new NotJoinedRuntimeException(Disk.class);
            }
            orderDto.items.add(OrderItem.newCreateTemplateOrderItem(av.vm));
        }

        return orderDto;
    }

    /**
     *
     * <p>{@link ApplicationInstanceGroup} に所属する
     * {@link ApplicationInstance}が所有する
     * {@link ApplicationInstanceVm}の作成に必要なリソースに対する要求条件を作成します.
     * <br>
     * 下記が前提となります.
     * <ul>
     * <li>ApplicationInstanceGroupにapplicationInstanceListが設定されている。
     * <li>ApplicationInstanceGroupにapplicationが設定されている。
     * <li>applicationにApplicationVmListが設定されている。
     * <li>ApplicationVmにvmが設定されている.
     * <li>vmにdiskListが設定されている.
     * <li>vmにdiskListが設定されている.
     * </ul>
     * <br>
     * 作成した要求条件はCloudLogicに依頼して予約番号に変換します.
     *
     * NEXT:OrderItemのtypeがCREATE_TEMPLATEになっている。本来の意味と異なるため要修正
     *
     * @param aig 作成予定の{@link jp.co.ntts.vhut.entity.ApplicationInstanceGroup}
     * @return 要求条件
     */
    public static final OrderDto createOrderToCreateApplicationInstanceGroup(ApplicationInstanceGroup aig) {

        OrderDto orderDto = createOrderToCreateApplication(aig.application);

        int size = 0;
        if (aig.applicationInstanceList != null) {
            size = aig.applicationInstanceList.size();
        }

        return orderDto.multiply(size);
    }

    /**
     * <p>{@link Application}が所有する
     * {@link ApplicationVm}の起動に必要なリソースに対する要求条件を作成します.
     * <br>
     * 作成した要求条件はCloudLogicに依頼して予約番号に変換します.
     *
     * @param app 作成予定の{@link Application}
     * @param startTime 起動の開始時間
     * @param endTime 起動の終了時間
     * @param exIpRequestMode 外部IPの申請モード
     * @return 要求条件
     */
    public static final OrderDto createOrderToStart(Application app, Timestamp startTime, Timestamp endTime, ExternalIpRequestMode exIpRequestMode) {
        HotdeployUtil.start();
        OrderDto orderDto = SingletonS2Container.getComponent(OrderDto.class);
        HotdeployUtil.stop();

        orderDto.startTime = startTime;
        orderDto.endTime = endTime;

        if (app.applicationVmList == null) {
            throw new NotJoinedRuntimeException(ApplicationVm.class);
        }

        if (app.applicationSecurityGroupList == null) {
            throw new NotJoinedRuntimeException(ApplicationSecurityGroup.class);
        }

        orderDto.items = new ArrayList<OrderItem>();
        //        int ipCount = 0;

        // VM起動用の注文を作成
        for (ApplicationVm avm : app.applicationVmList) {
            if (avm.vm == null) {
                throw new NotJoinedRuntimeException(Vm.class);
            }
            if (avm.applicationVmSecurityGroupMapList == null) {
                throw new NotJoinedRuntimeException(ApplicationVmSecurityGroupMap.class);
            }
            orderDto.items.add(OrderItem.newStartVmOrderItem(avm.vm));
            // VM起動用の外部IPの注文を作成
            switch (exIpRequestMode) {
                case AUTO:
                    orderDto.items.add(OrderItem.newObtainPublicIpOrderItem(avm.vm, avm.applicationVmSecurityGroupMapList.size()));
                    break;
                case PERVM:
                    orderDto.items.add(OrderItem.newObtainPublicIpOrderItem(avm.vm, 1));
                    break;
                default:
                    break;
            }
        }

        // VM起動用のNetworkの注文を作成
        for (ApplicationSecurityGroup asg : app.applicationSecurityGroupList) {
            if (asg.securityGroup == null) {
                throw new NotJoinedRuntimeException(Network.class);
            }
            if (asg.applicationVmSecurityGroupMapList == null) {
                throw new NotJoinedRuntimeException(Network.class);
            }
            orderDto.items.add(OrderItem.newObtainNetworkOrderItem(asg.securityGroup));
            //            ipCount += asg.applicationVmSecurityGroupMapList.size();
        }

        return orderDto;
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
