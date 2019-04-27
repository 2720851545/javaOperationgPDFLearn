import org.apache.commons.io.FileUtils;
import org.icepdf.core.pobjects.Catalog;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.PInfo;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.text.PageText;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

/**
 * @program: demo8
 * @description:
 * @author: liuwei
 * @create: 2019-04-26 16:46
 **/
public class ICEpdfTest {

    static String baseDir = null;
    static Document document = new Document();
    static String file = "target/demo";
    static String file1 = "target/demo1";

    @BeforeClass
    public static void beforeClass() throws Exception{
        baseDir = System.getProperty("user.dir");
//        使用网络文件(不是响应code 302重定向的)
//        document.setFile();
        document.setFile("src/main/resources/demo.pdf");

        new File(file).mkdirs();
        new File(file1).mkdirs();
        FileUtils.cleanDirectory(new File(file));
        FileUtils.cleanDirectory(new File(file1));
    }

    /**
     * pdf文件一共有多少页
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        System.out.println("document.getNumberOfPages() = " + document.getNumberOfPages());
//        document.setFile();
//        System.out.println(document.getDocumentLocation());
    }


    /**
     * 每一页转换成图片
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage image = (BufferedImage) document.getPageImage(
                    i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, 0, 1);
            ImageIO.write(image, "png", new File(String.format("%s/%s.png", file,i)));
        }
        document.dispose();
    }

    /**
     * 获取每一页的图片
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            List<Image> imageList = document.getPageImages(1);
            System.out.println("imageList.size() = " + imageList.size());
            for (int i1 = 0; i1 < imageList.size(); i1++) {
                BufferedImage image = (BufferedImage) imageList.get(i1);
                ImageIO.write(image, "png", new File(String.format("%s/%s-%s.png", file1,i, i1)));
            }
        }
    }

    /**
     * 获取每一页的文本内容
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            PageText pageText1 = document.getPageViewText(i);
//            使用getPageText可能会出现内容多余空格, 虽然getPageText更快
//            PageText pageText = document.getPageText(i);

            System.out.println(pageText1.toString());
        }
    }


    @AfterClass
    public static void AfterClass(){
        document.dispose();
    }

}
