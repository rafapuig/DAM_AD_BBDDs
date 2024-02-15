package dam.ad.converters;

import java.text.DecimalFormat;

public class FloatRowConverter implements RowConverter<Float>{
    @Override
    public String getAsRow(Float aFloat) {
        return new DecimalFormat("#,##0.000").format(aFloat);
    }
}
