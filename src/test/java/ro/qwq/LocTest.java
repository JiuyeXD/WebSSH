package ro.qwq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.qwq.webssh.Utils.NetworkUtils;

import java.io.IOException;

/**
 * <p>
 * LocTest - TODO
 * </p>
 *
 * @author JiuyeXD
 * @version 1.0
 * @since 2022/1/6
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocTest {

    @Test
    public void getAddressByBD2() {
        System.out.println(NetworkUtils.getAddressByBD2("125.80.0.0"));
        System.out.println(NetworkUtils.removeNetworkOperator(NetworkUtils.getAddressByBD2("42.196.0.0")));
        System.out.println(NetworkUtils.getAddressByBD2("1.0.32.1"));
    }
}
