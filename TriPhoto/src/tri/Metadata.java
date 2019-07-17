package tri;
import java.io.File;
import java.io.IOException;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;

public class Metadata {
	
    public static String RenvoiDateToString(final File file) throws ImageReadException,
            IOException {
        // get all metadata stored in EXIF format (ie. from JPEG or TIFF).
        String d = "";
        try {
        	final ImageMetadata metadata = Imaging.getMetadata(file);
        	if (metadata instanceof JpegImageMetadata) {
                final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;                    
                d= renvoiTagValue(jpegMetadata,ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
               	if (d.length() == 0) {d="";}
            } else {
            	d="";
            }       		
        }
        catch (ImageReadException i) {
        	d = "";
        }
        return d;
    }

    private static String renvoiTagValue(final JpegImageMetadata jpegMetadata,
            final TagInfo tagInfo) {
        final TiffField field = jpegMetadata.findEXIFValueWithExactMatch(tagInfo);
        String s = "";
        if (field != null) {
            s = field.getValueDescription();
        } 
        return s;
    }

}
