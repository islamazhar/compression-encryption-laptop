package utilities;

import com.javamex.classmexer.MemoryUtil;

public class MemoryTester{
    public static void main(String[] args) {
        Integer a = new Integer(123456);
        System.out.println("Memory = "+ MemoryUtil.memoryUsageOf(a));
    }
}
