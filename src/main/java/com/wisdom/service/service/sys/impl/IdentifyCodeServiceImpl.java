package com.wisdom.service.service.sys.impl;

import com.wisdom.common.constants.CommonConstant;
import com.wisdom.common.constants.SysParamDetailConstant;
import com.wisdom.common.exception.ApplicationException;
import com.wisdom.common.util.DateUtil;
import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.entity.IdentifyCode;
import com.wisdom.dao.entity.IdentifyCodeExample;
import com.wisdom.dao.mapper.IdentifyCodeMapper;
import com.wisdom.service.service.sys.IIdentifyCodeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 验证码
 * Created by fusj on 16/3/16.
 */
@Service
@Transactional
public class IdentifyCodeServiceImpl implements IIdentifyCodeService {

    @Autowired
    private IdentifyCodeMapper identifyCodeMapper;

    /**
     * 发送验证码
     * @param phoneNo
     * @param type
     */
    @Override
    public String sendIdentifyCode(String phoneNo, String type, String ip) {
        String code;

        IdentifyCodeExample example = new IdentifyCodeExample();
        example.createCriteria().andPhoneNoEqualTo(phoneNo)
                .andTypeEqualTo(type);

        List<IdentifyCode> list = identifyCodeMapper.selectByExample(example);
        // 数据库中不存在
        if(CollectionUtils.isEmpty(list)) {
            IdentifyCode identifyCode = new IdentifyCode();

            identifyCode.setType(type);
            identifyCode.setPhoneNo(phoneNo);
            identifyCode.setIpCode(ip);
            identifyCode.setCode(StringUtil.genIdentifyCode());
            identifyCode.setTime(1);
            identifyCode.setCreateTime(DateUtil.getTimestamp());
            // 验证码过期时间15分钟
            identifyCode.setInvalidTime(DateUtil.getTimestampByMinutes(CommonConstant.IDENTIFY_CODE_INVALID));
            identifyCode.setIsUsed(SysParamDetailConstant.IS_USED_FALSE);

            identifyCodeMapper.insertSelective(identifyCode);

            // 验证码
            code = identifyCode.getCode();
        }
        // 已经存在
        else {
            IdentifyCode identifyCode = list.get(0);

            // 已使用或已过期，重新生成验证码，并重新设定时间
            if(SysParamDetailConstant.IS_USED_TRUE.equals(identifyCode.getIsUsed()) ||
                    !DateUtil.isValid(identifyCode.getInvalidTime())) {

                identifyCode.setIpCode(ip);
                identifyCode.setCode(StringUtil.genIdentifyCode());
                identifyCode.setTime(1);
                identifyCode.setCreateTime(DateUtil.getTimestamp());
                // 验证码过期时间15分钟
                identifyCode.setInvalidTime(DateUtil.getTimestampByMinutes(CommonConstant.IDENTIFY_CODE_INVALID));
                identifyCode.setIsUsed(SysParamDetailConstant.IS_USED_FALSE);
                identifyCode.setUsedTime(null);

                identifyCodeMapper.updateByPrimaryKey(identifyCode);

                code = identifyCode.getCode();
            }
            // 未过期，再发送一遍相同的验证码，连续发送次数（time）+1，如果time>=设定次数，不发送密码，提示
            else {
                if(identifyCode.getTime() >= CommonConstant.SEND_IDENTIFY_TIMES) {
                    throw new ApplicationException("该手机号码" + CommonConstant.IDENTIFY_CODE_INVALID + "分钟内发送次数过多,请稍后再发");
                }

                identifyCode.setIpCode(ip);
                identifyCode.setTime(identifyCode.getTime() + 1);
                identifyCodeMapper.updateByPrimaryKeySelective(identifyCode);

                code = identifyCode.getCode();
            }
        }

        return code;
    }

    /**
     * 校验验证码是否正确
     * @param phoneNo
     * @param code
     * @param type
     * @return
     */
    @Override
    public Boolean validIdentifyCode(String phoneNo, String code, String type) {
        IdentifyCodeExample example = new IdentifyCodeExample();
        example.createCriteria().andPhoneNoEqualTo(phoneNo)
                .andTypeEqualTo(type);

        List<IdentifyCode> list = identifyCodeMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(list)) {
            return false;
        }

        IdentifyCode identifyCode = list.get(0);

        // 已过期或已使用
        if(SysParamDetailConstant.IS_USED_TRUE.equals(identifyCode.getIsUsed()) ||
                !DateUtil.isValid(identifyCode.getInvalidTime())) {
            return false;
        }

        // 验证码是否一致
        if(!identifyCode.getCode().equals(code)) {
            return false;
        }

        // 修改为已使用，并设定使用时间
        identifyCode.setIsUsed(SysParamDetailConstant.IS_USED_TRUE);
        identifyCode.setUsedTime(DateUtil.getTimestamp());

        identifyCodeMapper.updateByPrimaryKeySelective(identifyCode);

        return true;
    }
}
