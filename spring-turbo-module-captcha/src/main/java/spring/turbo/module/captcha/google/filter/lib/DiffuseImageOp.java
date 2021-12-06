/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   ___ | '_ | '__| | '_  / _` || || | | | '__| '_  / _
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|__, ||_| __,_|_|  |_.__/ ___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha.google.filter.lib;

import java.util.Random;

/**
 * @author Piotr Piastucki
 * @since 1.0.0
 */
public class DiffuseImageOp extends AbstractTransformImageOp {

    private double[] tx;
    private double[] ty;
    private double amount;

    public DiffuseImageOp() {
        amount = 1.6;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    protected synchronized void init() {
        tx = new double[256];
        ty = new double[256];
        for (int i = 0; i < 256; i++) {
            double angle = 2 * Math.PI * i / 256;
            tx[i] = amount * Math.sin(angle);
            ty[i] = amount * Math.cos(angle);
        }
    }

    @Override
    protected void transform(int x, int y, double[] t) {
        Random r = new Random();
        int angle = (int) (r.nextFloat() * 255);
        t[0] = x + tx[angle];
        t[1] = y + ty[angle];
    }

}
