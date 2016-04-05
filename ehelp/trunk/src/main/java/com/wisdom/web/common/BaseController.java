package com.wisdom.web.common;

import com.wisdom.common.entity.ResultBean;
import com.wisdom.common.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

/**
 *
 * Created by fusj on 16/3/2.
 */
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 后台管理VM路径
     */
    protected static final String MANAGER_VM_ROOT = "/help/%s";

    /**
     * 手机端VM路径
     */
    protected static final String PHONE_VM_ROOT = "/phone/%s";

    /**
     * 异常处理
     * @param request
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler
    public String doHandle(HttpServletRequest request, Exception ex) throws Exception {

        logger.error(ex.getMessage(), ex);

        request.setAttribute("ex", ex);

        if (request.getHeader("x-requested-with") != null) {
            throw ex;
        }

        return "error/500";
    }

    /**
     * VO绑定处理
     * @param binder
     * @param request
     */
    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        SimpleDateFormat datetimeFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        datetimeFormat.setLenient(false);

        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(
                dateFormat, true));
        binder.registerCustomEditor(java.sql.Timestamp.class,
                new CustomTimestampEditor(datetimeFormat, true));
    }

    /**
     * ajax请求异常返回
     * @param ex
     * @return
     */
    protected ResultBean ajaxException(Exception ex) {
        logger.error(ex.getMessage(), ex);

        ResultBean resultBean = new ResultBean(false);

        if(ex instanceof ApplicationException) {
            resultBean.setMsg(ex.getMessage());
        } else {
            resultBean.setMsg("系统出错了");
        }

        return resultBean;
    }
}
