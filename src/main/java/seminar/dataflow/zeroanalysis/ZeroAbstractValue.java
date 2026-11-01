package seminar.dataflow.zeroanalysis;

/**
 * This enum represents the possible values of the zero analysis for a variable.
 * This is the extended lattice: besides -, 0 and +, it can say that a variable is <= 0, >= 0 or != 0.
 */
public enum ZeroAbstractValue {

    /**
     * We don't have information about the variable.
     */
    BOTTOM("bottom"),

    /**
     * The variable is negative (< 0).
     */
    NEGATIVE("negative"),

    /**
     * The variable is zero.
     */
    ZERO("zero"),

    /**
     * The variable is positive (> 0).
     */
    POSITIVE("positive"),

    /**
     * The variable is negative or zero (<= 0).
     */
    NON_POSITIVE("non-positive"),

    /**
     * The variable is positive or zero (>= 0).
     */
    NON_NEGATIVE("non-negative"),

    /**
     * The variable is not zero (< 0 or > 0).
     */
    NOT_ZERO("not-zero"),

    /**
     * The variable is top (can be any value).
     */
    TOP("top");


    /**
     * The name of the ZeroAbstractValue.
     */
    private final String name;

    @Override
    public String toString() {
        return this.name;
    }

    ZeroAbstractValue(String name) {
        this.name = name;
    }

    /**
     * Returns the result of the addition between this ZeroAbstractValue and another.
     * @param another the other ZeroAbstractValue.
     * @return the result of the addition.
     */
    public ZeroAbstractValue add(ZeroAbstractValue another) {
                
        if(this == BOTTOM || another == BOTTOM){
            return BOTTOM;
        }

        if(this == ZERO){
            return another;
        }

        if(another == ZERO){
            return this;
        }

        if ((this == POSITIVE && another == POSITIVE) 
            || (this == POSITIVE && another == NON_NEGATIVE) 
            || (this == NON_NEGATIVE && another == POSITIVE)) {
            return POSITIVE;
        }

        if ((this == NEGATIVE && another == NEGATIVE) 
            || (this == NEGATIVE && another == NON_POSITIVE) 
            || (this == NON_POSITIVE && another == NEGATIVE)) {
            return NEGATIVE;
        }

        if(this == NON_POSITIVE && another == NON_POSITIVE){
            return NON_POSITIVE;
        }

        if(this == NON_NEGATIVE && another == NON_NEGATIVE){
            return NON_NEGATIVE;
        }

        return TOP;
    }

    /**
     * Returns the result of the division between this ZeroAbstractValue and another.
     * @param another the other ZeroAbstractValue.
     * @return the result of the division.
     */
    public ZeroAbstractValue divideBy(ZeroAbstractValue another) {

        if (this == BOTTOM || another == BOTTOM) {
            return BOTTOM;
        }

        if (another == ZERO) {
            return BOTTOM;
        }

        if (this == ZERO) {
            return ZERO;
        }

        if (((this == POSITIVE || this == NON_NEGATIVE) && (another == POSITIVE || another == NON_NEGATIVE))
            || ((this == NEGATIVE || this == NON_POSITIVE) && (another == NEGATIVE || another == NON_POSITIVE))) {
            return NON_NEGATIVE;
        }

        if (((this == POSITIVE || this == NON_NEGATIVE) && (another == NEGATIVE || another == NON_POSITIVE))
            || ((this == NEGATIVE || this == NON_POSITIVE) && (another == POSITIVE || another == NON_NEGATIVE))) {
            return NON_POSITIVE;
        }

        return TOP;
    }

    /**
     * Returns the result of the multiplication between this ZeroAbstractValue and another.
     * @param another the other ZeroAbstractValue.
     * @return the result of the multiplication.
     */
    public ZeroAbstractValue multiplyBy(ZeroAbstractValue another) {
    
        if(this == BOTTOM || another == BOTTOM){
            return BOTTOM;
        }        
        
        if(this == ZERO || another == ZERO){
            return ZERO;
        }

        if ((this == POSITIVE && another == POSITIVE) || (this == NEGATIVE && another == NEGATIVE)) {
            return POSITIVE;
        }

        if ((this == POSITIVE && another == NEGATIVE) || (this == NEGATIVE && another == POSITIVE)) {
            return NEGATIVE;
        }

        /*there are two cases that are not correct in logic, but the algorithm is still correct since they have already been declared above */
        /*So two "errors" are retained for the sake of code conciseness and aesthetics*/
        if ((((this == NON_POSITIVE || this == NEGATIVE) && (another == POSITIVE || another == NON_NEGATIVE))) 
            || (((this == NON_NEGATIVE || this == POSITIVE) && (another == NEGATIVE || another == NON_POSITIVE)))){
            return NON_POSITIVE;
        }

        if ((((this == NON_NEGATIVE || this == POSITIVE) && (another == POSITIVE || another == NON_NEGATIVE))) 
            || (((this == NON_POSITIVE || this == NEGATIVE) && (another == NEGATIVE || another == NON_POSITIVE)))) {
            return NON_NEGATIVE;
        }     

        if ((this == NOT_ZERO && (another == POSITIVE || another == NEGATIVE || another == NOT_ZERO))
            || (another == NOT_ZERO && (this == POSITIVE || this == NEGATIVE || this == NOT_ZERO))) {
            return NOT_ZERO;
        }

        return TOP;
    }

    /**
     * Returns the result of the subtraction between this ZeroAbstractValue and another.
     * @param another the other ZeroAbstractValue.
     * @return the result of the subtraction.
     */
    public ZeroAbstractValue substract(ZeroAbstractValue another) {

        if (this == BOTTOM || another == BOTTOM) {
            return BOTTOM;
        }

        if (another == ZERO) {
            return this;
        }

        if (this == ZERO && another == NOT_ZERO) {
            return NOT_ZERO;
        }

        if ((this == ZERO && another == NON_POSITIVE) || (this == NON_NEGATIVE && another == NON_POSITIVE)) {
            return NON_NEGATIVE;
        }

        if ((this == ZERO && another == NON_NEGATIVE) || (this == NON_POSITIVE && another == NON_NEGATIVE)) {
            return NON_POSITIVE;
        }

        if (((this == ZERO || this == NON_NEGATIVE) && another == NEGATIVE)
            || (this == POSITIVE && (another == NON_POSITIVE || another == NEGATIVE))) {
            return POSITIVE;
        }

        if (((this == ZERO || this == NON_POSITIVE) && another == POSITIVE)
            || (this == NEGATIVE && (another == NON_NEGATIVE || another == POSITIVE))) {
            return NEGATIVE;
        }

        return TOP;
    }

    /**
     * Returns the result of the merge between this ZeroAbstractValue and another.
     * @param another the other ZeroAbstractValue.
     * @return the result of the merge.
     */
    public ZeroAbstractValue merge(ZeroAbstractValue another) {

        if(this == BOTTOM){
            return another;
        }

        if(another == BOTTOM){
            return this;
        }

        if(this == POSITIVE && another == POSITIVE){
            return POSITIVE;
        }

        if(this == NEGATIVE && another == NEGATIVE){
            return NEGATIVE;
        }

        if(this == ZERO && another == ZERO){
            return ZERO;
        }

        if((this == POSITIVE && another == NEGATIVE) || (this == NEGATIVE && another == POSITIVE)){
            return NOT_ZERO;
        }

        if ((this == NOT_ZERO && (another == POSITIVE || another == NEGATIVE || another == NOT_ZERO))
            || (another == NOT_ZERO && (this == POSITIVE || this == NEGATIVE || this == NOT_ZERO))) {
            return NOT_ZERO;
        }

        if((this == POSITIVE || this == ZERO || this == NON_NEGATIVE) 
            && (another == POSITIVE || another == ZERO || another == NON_NEGATIVE)){
            return NON_NEGATIVE;
        }

        if((this == NEGATIVE || this == ZERO || this == NON_POSITIVE) 
            && (another == NEGATIVE || another == ZERO || another == NON_POSITIVE)){
            return NON_POSITIVE;
        }

        return TOP;
    }
}
