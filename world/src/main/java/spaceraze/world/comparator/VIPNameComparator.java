/*
 * Created on 2005-aug-26
 */
package spaceraze.world.comparator;

import java.util.Comparator;

import spaceraze.world.VIP;

/**
 * @author WMPABOD
 *
 * Compares two viptypes, alfanumerically on name field
 */
public class VIPNameComparator<T extends VIP> implements Comparator<T> {
    static final long serialVersionUID = 1L;

	public int compare(T vip1, T vip2) {
//		VIP v1 = (VIP)arg0;
//		VIP v2 = (VIP)arg1;
//		return v1.getName().compareToIgnoreCase(v2.getName());
		return vip1.getName().compareToIgnoreCase(vip2.getName());
	}

}
