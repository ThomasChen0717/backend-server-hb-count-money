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
    /** 选择石头 */
    int selectStone = 13;
    String selectStoneExecutorName = "selectStone";// 执行器名称
    /** 售卖石头 */
    int sellStone = 14;
    String sellStoneExecutorName = "sellStone";// 执行器名称
    /** 连点次数增加 */
    int addClickCount = 15;
    String addClickCountExecutorName = "addClickCount";// 执行器名称
    /** 载具（新）升级 */
    int vehicleNewLevelUp = 16;
    String vehicleNewLevelUpExecutorName = "vehicleNewLevelUp";// 执行器名称
    /** 购买彩票 */
    int lotteryTicketBuy = 17;
    String lotteryTicketBuyExecutorName = "lotteryTicketBuy";// 执行器名称
    /** 领取彩金 */
    int lotteryTicketBonusGet = 18;
    String lotteryTicketBonusGetExecutorName = "lotteryTicketBonusGet";// 执行器名称
    /** 用户数据从内存保存到数据库 */
    int userDataFromCacheToDB = 19;
    String userDataFromCacheToDBExecutorName = "userDataFromCacheToDB";// 执行器名称

    /** 同步金钱（推送） */
    int moneySyncPush = 100;
    /** 同步称号（推送） */
    int titleSyncPush = 101;
    /** vip等级（推送） */
    int vipSyncPush = 102;
}
