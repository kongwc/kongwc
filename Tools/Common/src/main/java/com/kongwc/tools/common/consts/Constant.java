package com.kongwc.tools.common.consts;

/**
 * 常量定义
 */
public class Constant {

    /**
     * 前端设备类型
     */
    public static class FrontDeviceType {

        public static final int RTSP = 1;
        public static final int IPC = 2;
        public static final int FILE = 3;
        public static final int DIRECTORY = 4;
    }

    /**
     * 设备是否为是智能设备
     */
    public static class DevSmart {

        public static final int YES = 1;
        public static final int NO = 2;
    }

    /**
     * 设备是否为是32位设备
     */
    public static class Dev32Bit {

        public static final int YES = 1;
        public static final int NO = 2;
    }

    /**
     * 消息数据类型
     */
    public static class DataType {
        public static final int NOTHING = 0;

        public static final int FACE = 2;
        public static final int VEH = 3;
        public static final int OBJECT = 4;
        public static final int BEHAVIOR = 5;

        public static final int FACE_FULL = 20;
        public static final int FACE_RECOG = 21;
        public static final int FACE_COMPARE = 22;

        public static final int VEH_FULL = 30;
        public static final int VEH_RECOG = 31;
        public static final int VEH_COMPARE = 32;

        public static final int DEV_STS = 71;
        public static final int TASK_STS = 72;

        public static final int FACE_POSITION = 9000;
        public static final int ENDING = 9999;
    }

    /**
     * 数据来源类型
     */
    public static class DataSrcType {
        public static final int AUTO_CAPTURE = 1;
        public static final int MANUAL_SUBMIT = 2;
    }


    /**
     * 设备状态
     */
    public static class DeviceStatus {

        public static final int NONE = -1;
        public static final int UNKOWN = 0;
        public static final int ONLINE = 1;
        public static final int OFFLINE = 2;
        public static final int ERROR = 9;

    }

    /**
     * 设备类型
     */
    public static class DeviceType {

        public static final int FRONT = 1;
        public static final int PLATFORM = 2;
        public static final int ANLSVR = 3;
        public static final int MEDIASVR = 4;
        public static final int TRANSSVR = 5;

    }

    /**
     * 数据筛选动作类型
     */
    public static class DataFilterActionType {
        public static final int ACTION_DOWNLOAD = 1;
        public static final int ACTION_FILTER = 2;
    }

    /**
     * 任务类型
     */
    public static class TaskType {
        public static final int TASK_VIDEO_DOWNLOAD_AUTO = 1;
        public static final int TASK_VIDEO_DOWNLOAD_MANUAL = 2;
        public static final int TASK_SEARCH_BY_IMAGE = 3;
        public static final int TASK_VIDEO_TRANS_MANUAL = 4;
        public static final int TASK_VIDEO_DOWNLOAD_MANUAL_BY_DATA = 5;
    }

    public static class ApiReturnCode {

        public static final int OK = 0;
        public static final int ERROR_API_NOT_FOUND = 1;
        public static final int ERROR_PARAM_WRONG = 2;
        public static final int ERROR_API_ERROR = 3;
        public static final int ERROR_DATA_NOT_FOUND = 100;
        public static final int ERROR_SYSTEM = 999;
    }

    public static class StorageReturnCode {

        public static final int OK = 0;
        public static final int ERROR_SERVICE_NOT_FOUND = 1;
        public static final int ERROR_PARAM_WRONG = 2;
        public static final int ERROR_SERVICE_ERROR = 3;
        public static final int ERROR_RESOURCE_NOT_FOUND = 4;
        public static final int ERROR_SYSTEM = 999;
    }

    public static class DataIdLength {
        public static final int FACE_FULL = 33;
        public static final int FACE = 36;
        public static final int VEH_FULL = 33;
        public static final int VEH = 36;
        public static final int COMPARE_WITH_DB = 46;
        public static final int COMPARE = 53;
        public static final int FILE = 43;
    }

    public static class SysConfig {
        public static final String MESSAGE_QUEUE_CACHE_TIMEOUT = "MessageQueueCacheTimeout";
        public static final String SERVICE_INQUIRY_DEFAULT_TIME_BEFORE = "ServiceInquiryDefaultTimeBefore";
        public static final String SERVICE_INQUIRY_DEFAULT_LIMIT = "ServiceInquiryDefaultLimit";
        public static final String COMPARE_RESULT_TOP_COUNT = "CompareResultTopCount";
        public static final String COMPARE_RESULT_MIN_SCORE = "CompareResultMinScore";
        public static final String JOB_STATUS_CHECK_INTERVAL = "JobStatusCheckInterval";
        public static final String VIDEO_DOWNLOAD_TIME_RANGE = "VideoDownloadTimeRange";
        public static final String TASK_THREAD_COUNT = "TaskThreadCount";
        public static final String DATA_STATISTICS_UPDATE_INTERVAL = "DataStatisticsUpdateInterval";
        public static final String TARGET_DATABASE_DEFAULT_NAME = "TargetDatabaseDefaultName";
        public static final String TASK_STATUS_REPORT_INTERVAL = "TaskStatusReportInterval";
        public static final String TASK_FACE_SEARCH_SPEED = "TaskFaceSearchSpeed";
        public static final String CACHE_NAME = "*CacheName*";
    }

    public static class JobFrequency {
        public static final int ONE_TIME = 9;
        public static final int EVERY_DAY = 0;
        public static final int EVERY_WEEK = 1;
        public static final int EVERY_MONTH = 2;
    }

    public static class MediaCoderTaskStatus {
        public static final int TASK_CREATED = 0;
        public static final int FILE_UPLOADED = 2;
        public static final int TRANS_ING = 4;
        public static final int TRANS_END = 6;
        public static final int DOWNLOADED = 8;
        public static final int REMOVED = 900;
        public static final int ERROR = 999;
    }

    /**
     * 文件后缀
     */
    public static class FileExtension {
        public static final String JPG = "jpg";
    }

//    /**
//     * 文件保存子目录
//     */
//    public static class FileStorageSubPath {
//        public static final String IMAGE_CAPTURE = "capture";
//        public static final String IMAGE_TARGET = "target";
//        public static final String IMAGE_FROM_OUTSIDE = "outside";
//    }

    /**
     * 文件类型
     */
    public static class FileType {
        public static final int VIDEO = 1;
        public static final int IMAGE = 2;
        public static final int WORD = 3;
        public static final int EXCEL = 4;
        public static final int TEXT = 5;
        public static final int OTHER = 9;
    }

    /**
     * 文件上传类型
     */
    public static class FileUploadType {
        public static final int CAPTURE = 1;
        public static final int TARGET = 2;
        public static final int OTHER = 9;
    }

    /**
     * 排序
     */
    public static class Order {
        public static final String ASC = "1";
        public static final String DESC = "2";
    }

    /**
     * 消息代码
     */
    public static class MessageCode {
        // 未知错误
        public static final String UNKNOWN = "1000000";

        // -------------WebService错误代码定义----开始----------------------------------
        // API未找到
        public static final String API_NOT_FOUND = "1001001";
        // 服务内部错误
        public static final String SERVICE_INNER_ERROR = "1001002";
        // 服务参数错误
        public static final String SERVICE_PARAM_ERROR = "1001003";
        // 必须输入项目为空
        public static final String MUST_INPUT = "1001101";
        // 指定项目不存在
        public static final String NOT_EXSITS = "1001102";
        // 指定项目已经存在
        public static final String HAS_EXSITED = "1001103";
        // 指定项目不是数字
        public static final String NOT_NUMBERIC = "1001104";
        // -------------WebService错误代码定义-----结束---------------------------------
    }

    /**
     * 缓存操作
     */
    public static class CacheAction {
        public static final String CLEAR = "clear";
    }

    /**
     * 数据表
     */
    public static class DataTable {
        // 前端设备基础信息
        public static final String MST_FRONT = "MST_FRONT";
        // 视频平台基础信息
        public static final String MST_PLATFORM = "MST_PLATFORM";
        // 分析服务器基础信息
        public static final String MST_ANL_SVR = "MST_ANL_SVR";
        // 流媒体服务器基础信息
        public static final String MST_RMS_SVR = "MST_RMS_SVR";
        // 转码服务器基础信息
        public static final String MST_MC_SVR = "MST_MC_SVR";
        // 程序基础信息
        public static final String MST_PROG = "MST_PROG";
        // 设备适配程序信息
        public static final String MST_DEV_PROG = "MST_DEV_PROG";
        // 数据类型
        public static final String MST_META_DAT_TYPE = "MST_META_DAT_TYPE";
        // 数据属性
        public static final String MST_META_DAT_ATTR = "MST_META_DAT_ATTR";
        // 数据字典
        public static final String MST_META_DIC = "MST_META_DIC";
        // 数据字典属性
        public static final String MST_META_DIC_ATTR = "MST_META_DIC_ATTR";
        // 数据字典代码
        public static final String MST_META_DIC_CODE = "MST_META_DIC_CODE";
        // 数据属性转换方案
        public static final String MST_ATTR_TRANS_PLAN = "MST_ATTR_TRANS_PLAN";
        // 数据属性转换配置
        public static final String MST_ATTR_TRANS_CONF = "MST_ATTR_TRANS_CONF";
        // 图片结构化方案
        public static final String MST_RECOG_PLAN = "MST_RECOG_PLAN";
        // 图片结构化配置
        public static final String MST_RECOG_CONF = "MST_RECOG_CONF";
        // 图片比对方案
        public static final String MST_COMP_PLAN = "MST_COMP_PLAN";
        // 图片比对配置
        public static final String MST_COMP_CONF = "MST_COMP_CONF";
        // 系统参数配置
        public static final String MST_SYS_PARAM_CONF = "MST_SYS_PARAM_CONF";
        // 人脸比对底库
        public static final String MST_FACE_TARGET_DB = "MST_FACE_TARGET_DB";
        // 人脸比对底库对象
        public static final String MST_FACE_TARGET_DB_OBJ = "MST_FACE_TARGET_DB_OBJ";
        // 人脸目标
        public static final String MST_FACE_TARGET = "MST_FACE_TARGET";
        // 人脸目标对象
        public static final String MST_FACE_TARGET_OBJ = "MST_FACE_TARGET_OBJ";
        // 车辆比对底库
        public static final String MST_VEH_TARGET_DB = "MST_VEH_TARGET_DB";
        // 车辆目标
        public static final String MST_VEH_TARGET = "MST_VEH_TARGET";
        // 文件存储方案
        public static final String MST_STORAGE_PLAN = "MST_STORAGE_PLAN";
        // 文件存储配置
        public static final String MST_STORAGE_CONF = "MST_STORAGE_CONF";
        // 文件存储通道
        public static final String MST_STORAGE_CHANEL = "MST_STORAGE_CHANEL";
        // 采集任务配置
        public static final String MST_JOB_CONF = "MST_JOB_CONF";
        // 任务运行状态
        public static final String MST_TASK_STS = "MST_TASK_STS";
        // 设备运行状态
        public static final String MST_DEV_STS = "MST_DEV_STS";
        // 数据统计
        public static final String MST_DATA_STATS = "MST_DATA_STATS";
        // 结果筛选方案
        public static final String MST_RSLT_FILTER_PLAN = "MST_RSLT_FILTER_PLAN";
        // 结果筛选配置
        public static final String MST_RSLT_FILTER_CONF = "MST_RSLT_FILTER_CONF";
        // 任务流程方案
        public static final String MST_TASK_FLOW_PLAN = "MST_TASK_FLOW_PLAN";
        // 任务流程配置
        public static final String MST_TASK_FLOW_CONF = "MST_TASK_FLOW_CONF";
        // 人脸命中结果
        public static final String MST_FACE_HIT = "MST_FACE_HIT";
        // 车辆命中结果
        public static final String MST_VEH_HIT = "MST_VEH_HIT";
        // 区域分组
        public static final String MST_AREA_GRP = "MST_AREA_GRP";
        // 人脸抓拍原图
        public static final String WHS_FACE_FULL = "WHS_FACE_FULL";
        // 人脸抓拍结果
        public static final String WHS_FACE_CAP = "WHS_FACE_CAP";
        // 人脸比对结果
        public static final String WHS_FACE_COMP = "WHS_FACE_COMP";
        // 文件存储
        public static final String WHS_FILE_STORAGE = "WHS_FILE_STORAGE";
        // 车辆抓拍原图
        public static final String WHS_VEH_FULL = "WHS_VEH_FULL";
        // 车辆抓拍结果
        public static final String WHS_VEH_CAP = "WHS_VEH_CAP";
        // 车辆比对结果
        public static final String WHS_VEH_COMP = "WHS_VEH_COMP";
    }

    /**
     * 采集任务运行模式
     */
    public static class JobMode {
        public static final String FRONT = "front";
        public static final String BACKEND = "backend";
    }

    /**
     * 是否需要进行比对
     */
    public static class NeedCompare {
        public static final String YES = "1";
        public static final String NO = "2";
    }

    /**
     * 是否需要进行结构化
     */
    public static class NeedRecognize {
        public static final String YES = "1";
        public static final String NO = "2";
    }
}
