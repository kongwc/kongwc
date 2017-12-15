package com.kongwc.tools.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import java.io.Serializable;

/**
 * MessageQueue配置
 */
@Configuration
@Data
@PropertySource("classpath:msgqueue.properties")
public class MsgQueueConfig {

    /**
     * 连接Uri
     */
    @Value("${msgqueue.preprocess.uri}")
    private String preprocessUri;

    /**
     * 连接Uri
     */
    @Value("${msgqueue.process.uri}")
    private String processUri;

    /**
     * 预处理消息队列 - 人脸抓拍原图
     */
    @Value("${msgqueue.queue.preprocess.face.full}")
    private String queuePreprocessFaceFull;

    /**
     * 预处理消息队列交换机 - 人脸抓拍原图
     */
    @Value("${msgqueue.exchange.preprocess.face.full}")
    private String exchangePreprocessFaceFull;

    /**
     * 预处理消息队列 - 人脸抓拍数据
     */
    @Value("${msgqueue.queue.preprocess.face.capture}")
    private String queuePreprocessFaceCapture;

    /**
     * 预处理消息队列交换机 - 人脸抓拍数据
     */
    @Value("${msgqueue.exchange.preprocess.face.capture}")
    private String exchangePreprocessFaceCapture;

    /**
     * 预处理消息队列 - 人脸结构化数据
     */
    @Value("${msgqueue.queue.preprocess.face.recognize}")
    private String queuePreprocessFaceRecognize;

    /**
     * 预处理消息队列交换机 - 人脸结构化数据
     */
    @Value("${msgqueue.exchange.preprocess.face.recognize}")
    private String exchangePreprocessFaceRecognize;

    /**
     * 预处理消息队列 - 人脸比对数据
     */
    @Value("${msgqueue.queue.preprocess.face.compare}")
    private String queuePreprocessFaceCompare;

    /**
     * 预处理消息队列交换机 - 人脸比对数据
     */
    @Value("${msgqueue.exchange.preprocess.face.compare}")
    private String exchangePreprocessFaceCompare;

    /**
     * 应用处理消息队列 - 人脸抓拍结果处理
     */
    @Value("${msgqueue.queue.result.face.capture.process}")
    private String queueResultFaceCaptureProcess;

    /**
     * 应用处理消息队列 - 人脸抓拍结果展示
     */
    @Value("${msgqueue.queue.result.face.capture.report}")
    private String queueResultFaceCaptureReport;

//    /**
//     * 应用处理消息队列 - 人脸抓拍结果统计
//     */
//    @Value("${msgqueue.queue.result.face.capture.statistics}")
//    private String queueResultFaceCaptureStatistics;

    /**
     * 应用处理消息队列交换机 - 人脸抓拍结果
     */
    @Value("${msgqueue.exchange.result.face.capture}")
    private String exchangeResultFaceCapture;

    /**
     * 应用处理消息队列 - 人脸比对结果处理
     */
    @Value("${msgqueue.queue.result.face.compare.process}")
    private String queueResultFaceCompareProcess;

    /**
     * 应用处理消息队列 - 人脸比对结果录像抽取
     */
    @Value("${msgqueue.queue.result.face.compare.download}")
    private String queueResultFaceCompareDownload;

    /**
     * 应用处理消息队列 - 人脸比对结果筛选
     */
    @Value("${msgqueue.queue.result.face.compare.filter}")
    private String queueResultFaceCompareFilter;

    /**
     * 应用处理消息队列 - 人脸比对结果展示
     */
    @Value("${msgqueue.queue.result.face.compare.report}")
    private String queueResultFaceCompareReport;

//    /**
//     * 应用处理消息队列 - 人脸比对结果统计
//     */
//    @Value("${msgqueue.queue.result.face.compare.statistics}")
//    private String queueResultFaceCompareStatistics;

    /**
     * 应用处理消息队列交换机 - 人脸比对结果
     */
    @Value("${msgqueue.exchange.result.face.compare}")
    private String exchangeResultFaceCompare;

    /**
     * 预处理消息队列 - 车辆抓拍原图
     */
    @Value("${msgqueue.queue.preprocess.vehicle.full}")
    private String queuePreprocessVehicleFull;

    /**
     * 预处理消息队列交换机 - 车辆抓拍原图
     */
    @Value("${msgqueue.exchange.preprocess.vehicle.full}")
    private String exchangePreprocessVehicleFull;

    /**
     * 预处理消息队列 - 车辆抓拍数据
     */
    @Value("${msgqueue.queue.preprocess.vehicle.capture}")
    private String queuePreprocessVehicleCapture;

    /**
     * 预处理消息队列交换机 - 车辆抓拍数据
     */
    @Value("${msgqueue.exchange.preprocess.vehicle.capture}")
    private String exchangePreprocessVehicleCapture;

    /**
     * 预处理消息队列 - 车辆结构化数据
     */
    @Value("${msgqueue.queue.preprocess.vehicle.recognize}")
    private String queuePreprocessVehicleRecognize;

    /**
     * 预处理消息队列交换机 - 车辆结构化数据
     */
    @Value("${msgqueue.exchange.preprocess.vehicle.recognize}")
    private String exchangePreprocessVehicleRecognize;

    /**
     * 预处理消息队列 - 车辆比对数据
     */
    @Value("${msgqueue.queue.preprocess.vehicle.compare}")
    private String queuePreprocessVehicleCompare;

    /**
     * 预处理消息队列交换机 - 车辆比对数据
     */
    @Value("${msgqueue.exchange.preprocess.vehicle.compare}")
    private String exchangePreprocessVehicleCompare;

    /**
     * 应用处理消息队列 - 车辆抓拍结果处理
     */
    @Value("${msgqueue.queue.result.vehicle.capture.process}")
    private String queueResultVehicleCaptureProcess;

    /**
     * 应用处理消息队列 - 车辆抓拍结果展示
     */
    @Value("${msgqueue.queue.result.vehicle.capture.report}")
    private String queueResultVehicleCaptureReport;

//    /**
//     * 应用处理消息队列 - 车辆抓拍结果统计
//     */
//    @Value("${msgqueue.queue.result.vehicle.capture.statistics}")
//    private String queueResultVehicleCaptureStatistics;

    /**
     * 应用处理消息队列交换机 - 车辆抓拍结果
     */
    @Value("${msgqueue.exchange.result.vehicle.capture}")
    private String exchangeResultVehicleCapture;

    /**
     * 应用处理消息队列 - 车辆比对结果处理
     */
    @Value("${msgqueue.queue.result.vehicle.compare.process}")
    private String queueResultVehicleCompareProcess;

    /**
     * 应用处理消息队列 - 车辆比对结果录像抽取
     */
    @Value("${msgqueue.queue.result.vehicle.compare.download}")
    private String queueResultVehicleCompareDownload;

    /**
     * 应用处理消息队列 - 车辆比对结果筛选
     */
    @Value("${msgqueue.queue.result.vehicle.compare.filter}")
    private String queueResultVehicleCompareFilter;

    /**
     * 应用处理消息队列 - 车辆比对结果展示
     */
    @Value("${msgqueue.queue.result.vehicle.compare.report}")
    private String queueResultVehicleCompareReport;

//    /**
//     * 应用处理消息队列 - 车辆比对结果统计
//     */
//    @Value("${msgqueue.queue.result.vehicle.compare.statistics}")
//    private String queueResultVehicleCompareStatistics;

    /**
     * 应用处理消息队列交换机 - 车辆比对结果
     */
    @Value("${msgqueue.exchange.result.vehicle.compare}")
    private String exchangeResultVehicleCompare;

    /**
     * 应用处理消息队列 - 设备状态处理
     */
    @Value("${msgqueue.queue.device.status.process}")
    private String queueDeviceStatusProcess;

    /**
     * 应用处理消息队列交换机 - 设备状态处理
     */
    @Value("${msgqueue.exchange.device.status.process}")
    private String exchangeDeviceStatusProcess;

    /**
     * 应用处理消息队列 - 设备状态展示
     */
    @Value("${msgqueue.queue.device.status.report}")
    private String queueDeviceStatusReport;

    /**
     * 应用处理消息队列交换机 - 设备状态
     */
    @Value("${msgqueue.exchange.device.status.report}")
    private String exchangeDeviceStatusReport;

    /**
     * 应用处理消息队列 - 任务状态处理
     */
    @Value("${msgqueue.queue.task.status.process}")
    private String queueTaskStatusProcess;

    /**
     * 应用处理消息队列 - 任务状态展示
     */
    @Value("${msgqueue.queue.task.status.report}")
    private String queueTaskStatusReport;

    /**
     * 应用处理消息队列交换机 - 任务状态
     */
    @Value("${msgqueue.exchange.task.status}")
    private String exchangeTaskStatus;

    /**
     * 应用处理消息队列 - 任务状态处理
     */
    @Value("${msgqueue.queue.task.process}")
    private String queueTaskProcess;

    /**
     * 应用处理消息队列交换机 - 任务状态
     */
    @Value("${msgqueue.exchange.task.process}")
    private String exchangeTaskProcess;

    /**
     * 应用处理消息队列 - 人脸命中
     */
    @Value("${msgqueue.queue.hit.face.report}")
    private String queueHitFaceReport;

    /**
     * 应用处理消息队列交换机 - 人脸命中
     */
    @Value("${msgqueue.exchange.hit.face}")
    private String exchangeHitFace;

    /**
     * 应用处理消息队列 - 车辆命中
     */
    @Value("${msgqueue.queue.hit.vehicle.report}")
    private String queueHitVehicleReport;

    /**
     * 应用处理消息队列交换机 - 车辆命中
     */
    @Value("${msgqueue.exchange.hit.vehicle}")
    private String exchangeHitVehicle;

    /**
     * 应用处理消息队列 - 统计数据
     */
    @Value("${msgqueue.queue.statistics}")
    private String queueStatistics;

    /**
     * 应用处理消息队列交换机 - 统计数据
     */
    @Value("${msgqueue.exchange.statistics}")
    private String exchangeStatistics;

}
