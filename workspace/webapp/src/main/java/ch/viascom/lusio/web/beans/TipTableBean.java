package ch.viascom.lusio.web.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ch.viascom.lusio.business.DataServiceProvider;
import ch.viascom.lusio.module.TipModel;
import java.io.Serializable;

@Named("tipTableBean")
@SessionScoped
public class TipTableBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<TipModel> tips;

    private TipModel selectedTipModel;

    @Inject
    DataServiceProvider serviceProvider;

    public TipTableBean() {
        try {
            tips = serviceProvider.getLatestTips("83aeb73d-9cfe-4f65-b470-50628720bb1d");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public TipModel getSelectedTipModel() {
        return selectedTipModel;
    }

    public void setSelectedCar(TipModel selectedTipModel) {
        this.selectedTipModel = selectedTipModel;
    }

    public List<TipModel> getTips() {
        return this.tips;
    }
}