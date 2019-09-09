package core;

import com.github.chenlijia1111.utils.core.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/9 0009 下午 6:32
 **/
public class FileTest {

    @Test
    public void test1(){
        try {
            long l = System.currentTimeMillis();
            FileUtils.copyFile(new File("D:\\ssmProject\\waibao\\pageoffice\\target\\pageOffice.war"),new File("D:\\ssmProject\\waibao\\pageoffice\\target\\pageOffice1.war"));
            System.out.println(System.currentTimeMillis() - l);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
