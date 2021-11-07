package datamodel;

/**
 * Class that describes an ordered item as part of an Order. Orders may have multiple order items.
 * 
 * @since "0.1.0"
 * @version {@value package_info#Version}
 * @author Leander Wendt
 */
public class OrderItem {
    /**
     * ordered article, throws IllegalArgumentException if article is null
     */
    private final Article article;

    /**
     * number of articles ordered, throws IllegalArgumentException if not a positive number
     */
    private int unitsOrdered;




    /**
     * Constructor with article and units arguments.
     * @param article ordered article, throws IllegalArgumentException if article is null
     * @param unitsOrdered number of articles ordered
     */
    public OrderItem(Article article, int unitsOrdered) {
        this.article = article;
        if (unitsOrdered >= 0){
            this.unitsOrdered = unitsOrdered;
        }
    }

    /**
     * Article getter. Attribute article cannot be changed has therefore has no setter (immutable attribute).
     * @return reference to the ordered article
     */
    public Article getArticle() {
        return article;
    }

    /**
     * UnitsOrdered getter.
     * @return number of article ordered
     */
    public int getUnitsOrdered() {
        return unitsOrdered;
    }

    /**
     * UnitsOrdered setter with constraint: {@code units >= 0}, otherwise the method has no effect.
     * @param units updated number of articles ordered
     */
    public OrderItem setUnitsOrdered(int units) {
       if (units >= 0){
           this.unitsOrdered = units;
       }
        return this;
    }
}