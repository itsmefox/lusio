package ch.viascom.lusio;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import static org.mockito.Mockito.*;

import ch.viascom.base.exceptions.ServiceException;
import ch.viascom.lusio.beans.AccountDBBean;
import ch.viascom.lusio.beans.GameDBBean;
import ch.viascom.lusio.beans.RouletteBean;
import ch.viascom.lusio.beans.TipDBBean;
import ch.viascom.lusio.entity.Field;
import ch.viascom.lusio.entity.Tip;

public class ProcessGameMock {

    @Test
    public void processGame() throws ServiceException{
        
        RouletteBean rouletteBean = new RouletteBean();
        GameDBBean gameDBBean = mock(GameDBBean.class);
        TipDBBean tipDBBean = mock(TipDBBean.class);
        AccountDBBean accountDBBean = mock(AccountDBBean.class);
        
        Field field = new Field();
        field.setColor("B");
        field.setField_ID("cbe4abb0-8d77-11e2-aa93-8dee04485f01");
        field.setValue("26");
        
        Field black = new Field();
        black.setColor(null);
        black.setField_ID("07828728-8d78-11e2-aa93-8dee04485f01");
        black.setValue("BLACK");
        
        
        Tip tip = new Tip();
        tip.setAmount(200);
        tip.setDate(new Date());
        tip.setTip_ID("TEST-TIP");

        List<Tip> tips = new ArrayList<>();
        tips.add(tip);

        when(gameDBBean.getField("BLACK")).thenReturn(black);
        when(tipDBBean.getTipsByField(black, "1")).thenReturn(tips);
        
        System.out.println(rouletteBean.colorWins(0, "1", field.getColor()));
        
    }
}
