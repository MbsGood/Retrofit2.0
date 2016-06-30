package com.yunnex.merge.common;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class does the work of decoding the user's request and extracting all
 * the data to be encoded in a barcode.
 */
public final class BarcodeEncoder
{
	public static final String TEXT = "TEXT_TYPE";

	public static final String TAG = BarcodeEncoder.class.getSimpleName();

	private static final int WHITE = 0xFFFFFFFF;

	private static final int BLACK = 0xFF000000;

	private String contents;

	private BarcodeFormat format;

	private final int width;

	private final int height;

	private final boolean useVCard;

	public BarcodeEncoder(String data, int dimension, BarcodeFormat format) throws WriterException
	{
		this.width = dimension;
		this.height = dimension;
		this.useVCard = false;
		this.format = format;
		encodeContentsFromZXingIntent(data);
	}

	public BarcodeEncoder(String data, int width, int height, BarcodeFormat format) throws WriterException
	{
		this.width = width;
		this.height = height;
		this.useVCard = false;
		this.format = format;
		encodeContentsFromZXingIntent(data);
	}

	boolean isUseVCard()
	{
		return useVCard;
	}

	// It would be nice if the string encoding lived in the core ZXing library,
	// but we use platform specific code like PhoneNumberUtils, so it can't.
	private boolean encodeContentsFromZXingIntent(String data)
	{
		encodeQRCodeContents(data, TEXT);
		return contents != null && !contents.isEmpty();
	}

	private void encodeQRCodeContents(String data, String type)
	{
		if (type.equals(TEXT))
		{
			if (data != null && !data.isEmpty())
			{
				contents = data;
			}
		}
	}

	public Bitmap encodeAsBitmap() throws WriterException
	{
		String contentsToEncode = contents;
		Map<EncodeHintType, Object> hints = null;
		hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.MARGIN, 0);
		BitMatrix result;
		try
		{
			result = new MultiFormatWriter().encode(contentsToEncode, format, width, height, hints);
		}
		catch (IllegalArgumentException iae)
		{
			// Unsupported format
			return null;
		}
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++)
		{
			int offset = y * width;
			for (int x = 0; x < width; x++)
			{
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

}
