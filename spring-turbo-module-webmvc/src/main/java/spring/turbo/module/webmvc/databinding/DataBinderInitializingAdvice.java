package spring.turbo.module.webmvc.databinding;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import spring.turbo.databinding.SmartBindingErrorProcessor;

/**
 * @author 应卓
 * @since 3.3.1
 */
@ControllerAdvice
public class DataBinderInitializingAdvice {

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.setBindingErrorProcessor(SmartBindingErrorProcessor.getInstance());
    }

}
