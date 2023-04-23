package common.pb.cmd;

public interface UserCmdModule {
    int cmd = AllCmdModule.userCmd;

    /** 结算 */
    int settlement = 1;
    String settlementExecutorName = "settlement";// 结算报文的执行器名称
    /** 属性升级 */
    int attributeLevelUp = 2;
    String attributeLevelUpExecutorName = "attributeLevelUp";// 执行器名称
    /** 切换载具 */
    int changeVehicle = 3;
    String changeVehicleExecutorName = "changeVehicle";// 执行器名称
    /** 使用装备 */
    int useEquipment = 4;
    String useEquipmentExecutorName = "useEquipment";// 执行器名称

    /** 同步金钱（推送） */
    int moneySyncPush = 100;
}
