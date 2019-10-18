import com.eshanren.utils.StringUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/10/14.
 */
public class test {

    @Test
    public void test1 () {
        System.out.println(StringUtil.getLengthWithZh("a阿瑟a"));
    }

    @Test
    public void test2 () {
        System.out.println(StringUtil.getIndexWithZh("aa撒aasd",2,1));
    }

    @Test
    public void test3 () {
//        System.out.println(StringUtil.splitStr("skadjaldkjs,",","));
        List<String> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
//        System.out.println(StringUtil.arrayJoin(list,","));
        String[] s= {"1","2","3"};
        System.out.println(StringUtil.arrayJoin(s,","));
    }

    @Test
    public void test4() {
        String timeStr2= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
        System.out.println("当前时间为:"+timeStr2);
        SimpleDateFormat sf = null;
        String type = "date";
        if ("date".equals(type)) {
            sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        } else {
            sf = new SimpleDateFormat("yyyy年MM月");
        }
        Date date = new Date();
        try {
            date = sf.parse(timeStr2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date.getTime());

        int a = (int)((System.currentTimeMillis() - date.getTime()) / 86400000 );
        System.out.println(System.currentTimeMillis() - date.getTime());
        System.out.println(((1572071246000L - System.currentTimeMillis()) / 86400000));
        System.out.println(a);
    }


}
