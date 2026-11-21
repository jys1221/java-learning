import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * 知识点 19：IO 流 —— 读写文件
 *
 * 流的分类（记住这四个词就能串起来）：
 *
 *   按方向：输入流（读，InputStream/Reader）  输出流（写，OutputStream/Writer）
 *   按数据：字节流（处理一切二进制，InputStream/OutputStream）
 *          字符流（专门处理文本，Reader/Writer，自动处理编码）
 *
 * 常用组合：
 *   读文本文件：FileReader / BufferedReader
 *   写文本文件：FileWriter / BufferedWriter
 *   复制任意文件：FileInputStream + FileOutputStream
 *   读配置文件：Properties
 *
 * 核心原则：流用完必须 close()，推荐用 try-with-resources 自动关闭。
 */
public class FileIoDemo {

    public static void main(String[] args) {

        // 演示文件放在项目根目录下的 demo-io 文件夹，避免污染系统目录
        File dir = new File("demo-io");
        if (!dir.exists()) {
            dir.mkdirs();     // 创建多级目录
        }

        File textFile = new File(dir, "hello.txt");
        System.out.println("文件绝对路径：" + textFile.getAbsolutePath());

        // ---------- 一、File 类：只表示"路径"，不读写内容 ----------
        System.out.println("存在吗：" + textFile.exists());
        System.out.println("是文件吗：" + textFile.isFile());
        System.out.println("是目录吗：" + textFile.isDirectory());
        System.out.println("文件名：" + textFile.getName());
        System.out.println("父目录：" + textFile.getParent());

        // ---------- 二、字符流写文本 ----------
        // try-with-resources：括号里的流用完自动关闭
        try (FileWriter fw = new FileWriter(textFile)) {
            // FileWriter(File) 默认是"覆盖"模式；要追加就写 new FileWriter(file, true)
            fw.write("第一行：你好，Java IO\n");
            fw.write("第二行：中文写入测试\n");
        } catch (IOException e) {
            System.out.println("写入失败：" + e.getMessage());
        }

        // ---------- 三、字符流读文本 ----------
        try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
            String line;
            int lineNo = 1;
            // readLine 每次读一行，读完返回 null
            while ((line = br.readLine()) != null) {
                System.out.println("第" + lineNo + "行：" + line);
                lineNo++;
            }
        } catch (IOException e) {
            System.out.println("读取失败：" + e.getMessage());
        }

        // ---------- 四、字节流：复制任意文件（图片、pdf 都行） ----------
        File source = new File(dir, "source.bin");
        File copy = new File(dir, "copy.bin");

        // 先造一个测试二进制文件
        try (FileOutputStream fos = new FileOutputStream(source)) {
            for (int i = 0; i < 1024; i++) {
                fos.write(i % 256);      // 写入 1024 个字节
            }
        } catch (IOException e) {
            System.out.println("生成测试文件失败：" + e.getMessage());
        }

        // 标准复制流程：边读边写，读完一块写一块
        try (FileInputStream in = new FileInputStream(source);
             FileOutputStream out = new FileOutputStream(copy)) {

            byte[] buffer = new byte[1024];      // 缓冲区：一次读一批，比一个字节一个字节快得多
            int len;
            while ((len = in.read(buffer)) != -1) {   // read 返回实际读到的字节数，-1 表示读完
                out.write(buffer, 0, len);            // 只写实际读到的部分
            }
            System.out.println("文件复制完成，源大小=" + source.length() + " 字节，副本大小=" + copy.length() + " 字节");
        } catch (IOException e) {
            System.out.println("复制失败：" + e.getMessage());
        }

        // ---------- 五、BufferedWriter：写大量文本时必备 ----------
        // 普通的 FileWriter 每写一次就碰一次磁盘；BufferedWriter 会先攒在内存缓冲区
        File bufFile = new File(dir, "buffered.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(bufFile))) {
            for (int i = 1; i <= 5; i++) {
                bw.write("这是缓冲写入的第 " + i + " 行");
                bw.newLine();     // 换行，比写 "\n" 更规范（自动适配系统换行符）
            }
        } catch (IOException e) {
            System.out.println("缓冲写入失败：" + e.getMessage());
        }
        System.out.println("缓冲写入完成，文件大小：" + bufFile.length() + " 字节");

        // ---------- 六、Properties：读写 key=value 配置 ----------
        // Spring Boot 的 application.properties 本质就是 Properties 文件
        File propFile = new File(dir, "config.properties");

        // 写配置
        Properties props = new Properties();
        props.setProperty("db.url", "jdbc:mysql://localhost:3306/school");
        props.setProperty("db.username", "root");
        props.setProperty("db.password", "CHANGE_ME");
        try (FileOutputStream fos = new FileOutputStream(propFile)) {
            props.store(fos, "演示配置文件");     // 第二个参数是注释
        } catch (IOException e) {
            System.out.println("写配置失败：" + e.getMessage());
        }

        // 读配置
        Properties loaded = new Properties();
        try (FileInputStream fis = new FileInputStream(propFile)) {
            loaded.load(fis);
            System.out.println("读取 db.url = " + loaded.getProperty("db.url"));
            System.out.println("读取 db.username = " + loaded.getProperty("db.username", "默认值"));
        } catch (IOException e) {
            System.out.println("读配置失败：" + e.getMessage());
        }

        // ---------- 七、删除演示文件（保持目录干净） ----------
        // 只删我们自己造的文件，不动其他内容
        String[] names = {"hello.txt", "source.bin", "copy.bin", "buffered.txt", "config.properties"};
        for (String name : names) {
            File f = new File(dir, name);
            if (f.exists() && f.delete()) {
                System.out.println("已删除演示文件：" + name);
            }
        }
    }
}
