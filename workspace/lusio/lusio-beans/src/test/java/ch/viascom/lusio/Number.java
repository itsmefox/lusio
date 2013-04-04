package ch.viascom.lusio;

public class Number implements Comparable<Number>{

    public Integer number;
    public Integer counter;
    
    public Number(Integer number,Integer counter){
        this.number = number;
        this.counter = counter;
    }

    @Override
    public int compareTo( Number argument ) {
        if( number < argument.number )
            return -1;
        if( number > argument.number )
            return 1;
            
        return 0;
    }
}
