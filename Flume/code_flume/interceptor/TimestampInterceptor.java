package com.atguigu.ad.flume.interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimestampInterceptor implements Interceptor {

    private Pattern pattern;

    @Override
    public void initialize() {
        pattern = Pattern.compile(".*t=(\\d{13}).*");
    }

    @Override
    public Event intercept(Event event) {

        String log = new String(event.getBody(), StandardCharsets.UTF_8);

        //1.移除每个字段的双引号
        //去掉前后两个双引号
        String subLog = log.substring(1, log.length() - 1);

        //去掉各分隔符两侧的双引号，\u0001表示分隔符
        String result = subLog.replaceAll("\"\u0001\"", "\u0001");
        event.setBody(result.getBytes(StandardCharsets.UTF_8));

        //2.提取时间戳(利用正则表达式的分组捕获功能实现)
        Matcher matcher = pattern.matcher(result);
        if (matcher.matches()) {
            String ts = matcher.group(1);
            event.getHeaders().put("timestamp", ts);
        } else {
            return null;
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {

        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event next = iterator.next();
            Event intercept = intercept(next);
            if (intercept == null) {
                iterator.remove();
            }
        }

        return events;
    }
    @Override
    public void close() {

    }

    public static class Builder implements Interceptor.Builder{
        @Override
        public Interceptor build() {
            return new TimestampInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
