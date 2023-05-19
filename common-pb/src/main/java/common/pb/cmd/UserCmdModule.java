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
    /** buffTool开始使用或者结束使用 */
    int startOrEndBuffTool = 5;
    String startOrEndBuffToolToolExecutorName = "startOrEndBuffTool";// 执行器名称
    /** 领取天降红包 */
    int getRedPacket = 6;
    String getRedPacketExecutorName = "getRedPacket";// 执行器名称
    /** 富豪挑战成功 */
    int challengeMagnateSuccess = 7;
    String challengeMagnateSuccessExecutorName = "challengeMagnateSuccess";// 执行器名称
    /** boss挑战成功 */
    int challengeBossSuccess = 8;
    String challengeBossSuccessExecutorName = "challengeBossSuccess";// 执行器名称
    /** 解锁载具或装备 */
    int unlockVehicleOrEquipment = 9;
    String unlockVehicleOrEquipmentExecutorName = "unlockVehicleOrEquipment";// 执行器名称
    /** 逻辑心跳报文 */
    int logicHeartbeat = 10;
    String logicHeartbeatExecutorName = "logicHeartbeat";// 执行器名称
    /** GM命令报文 */
    int gmCommand = 11;
    String gmCommandExecutorName = "gmCommand";// 执行器名称
    /** 看完广告报文 */
    int watchedAd = 12;
    String watchedAdExecutorName = "watchedAd";// 执行器名称

    /** 同步金钱（推送） */
    int moneySyncPush = 100;
    /** 同步称号（推送） */
    int titleSyncPush = 101;
    /** vip等级（推送） */
    int vipSyncPush = 102;
}
