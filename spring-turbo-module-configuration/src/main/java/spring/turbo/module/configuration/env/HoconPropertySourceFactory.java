package spring.turbo.module.configuration.env;

/**
 * @author 应卓
 * @see YamlPropertySourceFactory
 * @since 2.1.3
 */
public class HoconPropertySourceFactory extends AbstractPropertySourceFactory {

    /**
     * 默认构造方法
     */
    public HoconPropertySourceFactory() {
        super(new HoconPropertySourceLoader());
    }

}
