package com.qf.ithub.service;

import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;

import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;


@Service
public class MessageService {
	
	//产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    @Value("${sms.accessKeyId}")
    private String accessKeyId;
    static final String accessKeySecret = "jNJmZs2cve6SsBHSnZYYEZPzX5IirK";

	public ResultDTO sendRegSms(String phonenum) throws Exception {
		System.out.println("--------"+accessKeyId);
		// AppResult result = null;

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        // 产生短信客户端
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phonenum);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("扩新");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_150182900");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        // 获得一个随机的4位数字的代码
        String code = StringUtils.getCheckCode(1000, 9999)+"";
        request.setTemplateParam("{'code':'"+code+"'}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        //Thread.sleep(5000);
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        System.out.println(sendSmsResponse.getMessage());
        if(sendSmsResponse.getCode().equals("OK")) {
            return ResultDTO.builder()
                    .data(code)
                    .status(HttpStatus.OK.value())
                    .build();
        }
        return ResultDTO.builder()
                .status(600)
                .message(sendSmsResponse.getMessage())
                .build();
	}

}
