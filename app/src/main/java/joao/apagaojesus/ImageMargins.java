package joao.apagaojesus;

/**
 * Created by Joao on 29-08-2015.
 */
public class ImageMargins {

    private int left, top, right, bottom;

    public ImageMargins(int left, int top, int right, int bottom){

        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public int getLeft(){

        return left;
    }

    public int getTop(){

        return top;
    }

    public int getRight(){

        return right;
    }

    public int getBottom(){

        return bottom;
    }
}
