package com.jxztev;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CombineImg {
    private Graphics2D g = null;

    /**
     * 导入本地图片到缓冲区
     */
    public BufferedImage loadImageLocal(String imgName) {
        try {
            return ImageIO.read(new File(imgName));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public BufferedImage modifyImagetogeter(BufferedImage b, BufferedImage d) {

        try {
            int w = b.getWidth();
            int h = b.getHeight();

            g = d.createGraphics();
            g.drawImage(b, 0, 0, w, h, null);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return d;
    }

    /**
     * 生成新图片到本地
     */
    public void writeImageLocal(String newImage, BufferedImage img) {
        if (newImage != null && img != null) {
            try {
                File outputfile = new File(newImage);
                ImageIO.write(img, "jpg", outputfile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {

        CombineImg combineImg = new CombineImg();

        BufferedImage baseImg = combineImg.loadImageLocal("E:\\部门项目\\46lkk\\11水雨情\\第二部分\\图片套合江西省界\\SEVP_NSMC_WXCL_ASC_E99_ACHN_LNO_PY_20190723091500000.jpg");
        BufferedImage overImg = combineImg.loadImageLocal("E:\\部门项目\\46lkk\\11水雨情\\第二部分\\图片套合江西省界\\省界.png");

        combineImg.writeImageLocal("E:\\部门项目\\46lkk\\11水雨情\\第二部分\\图片套合江西省界\\SEVP_NSMC_WXCL_ASC_E99_ACHN_LNO_PY_20190723091500000_1.jpg", combineImg.modifyImagetogeter(overImg, baseImg));
        //将多张图片合在一起
        System.out.println("success");
    }
}
