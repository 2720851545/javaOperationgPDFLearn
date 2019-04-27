import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionJavaScript;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.AfterClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @program: demo8
 * @description:
 * @author: liuwei
 * @create: 2019-04-27 22:35
 **/
public class PDFBoxTest {


//    以下是PDFBox的四个主要组件 -
//
//    PDFBox - 这是PDFBox的主要部分。 这包含与内容提取和操作相关的类和接口。
//    FontBox - 包含与字体相关的类和接口，使用这些类可以修改PDF文档的文本字体。
//    XmpBox - 包含处理XMP元数据的类和接口。
//    Preflight - 此组件用于根据PDF/A-1b标准验证PDF文件。原文出自【易百教程】


    static String pdfFile = "src/main/resources/demo.pdf";
    static String newPDFFile = System.getProperty("user.dir") + "/target/demo.pdf";


    /**
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
//       加载文件(返回的是一个对象,不是原来对象)
        PDDocument pdDocument = PDDocument.load(new File(pdfFile));
//      获取总页数
        System.out.println("pdDocument.getNumberOfPages() = " + pdDocument.getNumberOfPages());
//      获取PDF规范版本
        System.out.println("pdDocument.getVersion() = " + pdDocument.getVersion());
    }

    /**
     * 创建一个空pdf
     *
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        PDDocument pdDocument = new PDDocument();
//        添加一个页面
        pdDocument.addPage(new PDPage());
//        保存pdf
        pdDocument.save(new File(newPDFFile));
//        关闭资源流
        pdDocument.close();
    }

    /**
     * 在原来pdf添加一个页面
     *
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        PDDocument pdDocument = PDDocument.load(new File(pdfFile));

        pdDocument.addPage(new PDPage());

        pdDocument.save(newPDFFile);

        pdDocument.close();
//        pdDocument.addPage();
    }

    /**
     * 删除第一页pdf
     *
     * @throws IOException
     */
    @Test
    public void test4() throws IOException {
        PDDocument pdDocument = PDDocument.load(new File(pdfFile));

//        页面从0开始
        pdDocument.removePage(0);

        pdDocument.save(newPDFFile);
        pdDocument.close();
    }

    /**
     * 获取文档的基本信息
     *
     * @throws Exception
     */
    @Test
    public void test5() throws Exception {
        PDDocument pdDocument = PDDocument.load(new File(pdfFile));

//        文档信息对象
        PDDocumentInformation information = pdDocument.getDocumentInformation();
        System.out.println("information.getAuthor() = " + information.getAuthor());
        System.out.println("information.getTitle() = " + information.getTitle());
        System.out.println("information.getKeywords() = " + information.getKeywords());
        System.out.println("information.getModificationDate() = " + information.getModificationDate());
        System.out.println("information.getCreationDate() = " + information.getCreationDate());
        System.out.println("information.getCreator() = " + information.getCreator());

//        每个get都有对应的一个set方法
        information.setAuthor("大白");

        pdDocument.save(pdfFile);

        pdDocument.close();
    }

    @Test
    public void test6() throws Exception {
        PDDocument pdDocument = PDDocument.load(new File(pdfFile));

//        页面从0开始
        PDPage page = pdDocument.getPage(0);

//       创建内容流
        PDPageContentStream pdPageContentStream = new PDPageContentStream(pdDocument, page);
//        开始编写内容操作
        pdPageContentStream.beginText();


//        设置字体样式(必须先设置在添加内容)
        pdPageContentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
//        linux系统使用该命令
//        pdPageContentStream.setFont(PDType0Font.load(pdDocument, new File("/usr/share/fonts/truetype/ttf-bitstream-vera/Vera.ttf")), 14);
//        初始化内容添加位置
        pdPageContentStream.newLineAtOffset(250, 500);
//        添加中文可能会报错
        pdPageContentStream.showText("i love you");
//        新开一行(), 不然内容会在同一行(在上一次添加结束后面继续添加), (newLine会在上次开始的位置开始添加)
//        newLine位置是上一次开始的位置
//        newLineAtOffset位置是相对于上一次开始的位置
//        pdPageContentStream.newLine();
        pdPageContentStream.newLineAtOffset(0, 20);
        pdPageContentStream.showText("you love i");
//        结束编写内容操作
        pdPageContentStream.endText();
//        必须关闭(不然内容会写不进去)
        pdPageContentStream.close();

        pdDocument.save(newPDFFile);

        pdDocument.close();
    }


    @Test
    public void test7() throws IOException {
        PDDocument document = PDDocument.load(new File(newPDFFile));

        PDFTextStripper stripper = new PDFTextStripper();
        //返回pdf的所有文本
        System.out.println("stripper.getText(document) = " + stripper.getText(document));

        document.close();
    }

    /**
     * pdf 创建个图片页面
     *
     * @throws IOException
     */
    @Test
    public void test8() throws IOException {
        PDDocument document = new PDDocument();

        PDPage page = new PDPage();
        PDPageContentStream pdPageContentStream = new PDPageContentStream(document, page);
        pdPageContentStream.drawImage(PDImageXObject.createFromFile(
                System.getProperty("user.dir")
                        + "/src/main/resources/demo.jpeg",
                document), 0, 0);
        pdPageContentStream.close();

        document.addPage(page);

        document.save(newPDFFile);
        document.close();
    }

    /**
     * 加密pdf
     *
     * @throws IOException
     */
    @Test
    public void test9() throws IOException {
        PDDocument document = PDDocument.load(new File(pdfFile));


        document.protect(new StandardProtectionPolicy(
                "大白", "123", new AccessPermission()));

        document.save(newPDFFile);
        document.close();
    }

    @Test
    public void test10() throws IOException {
        PDDocument document = PDDocument.load(new File(pdfFile));

        document.getDocumentCatalog().
                setOpenAction(new PDActionJavaScript("app.alert( {cMsg: 'this is an example', nIcon: 3,\"\n" +
                        "         + \" nType: 0, cTitle: 'PDFBox Javascript example'} );"));

        document.save(newPDFFile);

        document.close();
    }

    /**
     * 将每页分割
     * @throws IOException
     */
    @Test
    public void test11() throws IOException {
        PDDocument document = PDDocument.load(new File(pdfFile));

        Splitter splitter = new Splitter();
        List<PDDocument> list = splitter.split(document);
        int i = 1;
        for (PDDocument pdDocument : list) {
            pdDocument.save(newPDFFile + "." +  i + ".pdf");
            i++;
            pdDocument.close();
        }

        document.close();
    }

    /**
     * 将多个文档合并
     * @throws IOException
     */
    @Test
    public void test12() throws IOException {
        PDDocument document = PDDocument.load(new File(pdfFile));

        PDFMergerUtility mergerUtility = new PDFMergerUtility();
        mergerUtility.setDestinationFileName(newPDFFile);
        for (int i = 1; i <= document.getNumberOfPages(); i++) {
            mergerUtility.addSource(newPDFFile + "." +  i + ".pdf");
        }
        mergerUtility.mergeDocuments();

        document.close();
    }

    /**
     * 将页面转换成图片
     * @throws IOException
     */
    @Test
    public void test13() throws IOException {
        PDDocument document = PDDocument.load(new File(pdfFile));

        PDFRenderer renderer = new PDFRenderer(document);
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            //第一页是0
            BufferedImage image = renderer.renderImage(i);
            ImageIO.write(image, "PNG", new File("target/1.png"));
        }

        document.close();
    }

    /**
     * 在第一页画个实心矩形
     * @throws IOException
     */
    @Test
    public void test14() throws IOException {
        PDDocument document = PDDocument.load(new File(pdfFile));
        PDPageContentStream pdPageContentStream = new PDPageContentStream(
                document, document.getPage(0));

        pdPageContentStream.setNonStrokingColor(Color.BLACK);


        pdPageContentStream.addRect(200, 650, 100, 100);
        pdPageContentStream.fill();

        pdPageContentStream.close();


        document.save(newPDFFile);
        document.close();
    }

}
