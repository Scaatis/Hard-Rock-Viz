package scaatis.rrr;

import java.util.Comparator;

public class ZSort implements Comparator<Drawable>{

    @Override
    public int compare(Drawable o1, Drawable o2) {
        return Integer.compare(o1.getZ(), o2.getZ());
    }
    
}
