package com.verafied;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

import com.verafied.library.NativeLibrary;

public class SoulseekConnector {
    public SoulseekConnector(String init) {
        if (!init(init)) {
            throw new IllegalStateException();
        }

    }

    private boolean init(String init) {
        String s = "Connected to C# Library";
        try (Arena arena = Arena.ofConfined()) {

            // Allocate off-heap memory
            MemorySegment nativeText = arena.allocateUtf8String(s);
            MemorySegment name = arena.allocateUtf8String("Username12349876");
            MemorySegment pass = arena.allocateUtf8String("Password12349876");
            // Access off-heap memory
            // for (int i = 0; i < s.length(); i++) {
            //     System.out.print((char) nativeText.get(ValueLayout.JAVA_BYTE, i));
            // }
            NativeLibrary.write_line(nativeText);
            try {
                String result = NativeLibrary.start_up(name, pass).getUtf8String(0);
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            
        } // Off-heap memory is deallocated
        catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public void close(){
        NativeLibrary.close();
    }
}
