package ieci.tdw.ispac.ispacweb.comparators;
import ieci.tdw.ispac.ispaclib.utils.DateUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import org.apache.log4j.Logger;
import org.displaytag.model.Cell;

public class InverseDateComparator implements Comparator {
	
	private static final Logger LOGGER = Logger.getLogger(InverseDateComparator.class);

    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    
	private Collator collator;
    public InverseDateComparator()
    {
        this(Collator.getInstance());
    }

    public InverseDateComparator(Collator collatorToUse)
    {
        collator = collatorToUse;
        collator.setStrength(0);
    }
	public int compare(Object object1, Object object2) {
		if((object1 instanceof Date) && (object2 instanceof Date)){
        	Date init = (Date)object1;
    		Date end = (Date)object2;
    		try {
    			return DateUtil.compare(end, init);
			} catch (Exception e) {
				LOGGER.error("ERROR. " + e.getMessage(), e);
			}
        }else if((object1 instanceof Cell) && (object2 instanceof Cell)){
    		try {
	        	Date init = (Date)formatter.parse(((Cell)object1).getStaticValue().toString().trim());
	        	Date end = (Date)formatter.parse(((Cell)object2).getStaticValue().toString().trim());
    			return DateUtil.compare(end, init);
			} catch (Exception e) {
				LOGGER.error("ERROR. " + e.getMessage(), e);
			}
        }else if((object1 instanceof String) && (object2 instanceof String)){
    		try {
	        	Date init = (Date)formatter.parse(StringUtils.trim((String)object1));
	        	Date end = (Date)formatter.parse(StringUtils.trim((String)object2));
    			return DateUtil.compare(end, init);
			} catch (Exception e) {
				LOGGER.error("ERROR. " + e.getMessage(), e);
			}
        	
        }else if((object1 instanceof Comparable) && (object2 instanceof Comparable))
        	return ((Comparable)object2).compareTo(object1);
        else 
        	return collator.compare(object2.toString(), object1.toString());
		return 0;
	}
}
