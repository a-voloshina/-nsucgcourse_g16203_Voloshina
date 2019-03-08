package ru.nsu.fit.g16203.voloshina.controller;

import ru.nsu.fit.g16203.voloshina.general.exception.OutOfFieldRangeException;
import ru.nsu.fit.g16203.voloshina.model.CellStatus;

public interface IController {

    void setAliveCell(int coordinateX, int coordinateY) throws OutOfFieldRangeException;

    void reverseCellState(int coordinateX, int coordinateY) throws OutOfFieldRangeException;

    CellStatus getCellStatus(int coordinateX, int coordinateY) throws OutOfFieldRangeException;

    Double getCellImpact(int coordinateX, int coordinateY) throws OutOfFieldRangeException;

    void next();

    void run();

    void stop();

    void resizeField(int newWidth, int newHeight);

    void clearField();

    Double getLIVE_BEGIN();

    void setLIVE_BEGIN(Double LIVE_BEGIN);

    Double getLIVE_END();

    void setLIVE_END(Double LIVE_END);

    Double getBIRTH_BEGIN();

    void setBIRTH_BEGIN(Double BIRTH_BEGIN);

    Double getBIRTH_END();

    void setBIRTH_END(Double BIRTH_END);

    Double getFST_IMPACT();

    void setFST_IMPACT(Double FST_IMPACT);

    Double getSND_IMPACT();

    void setSND_IMPACT(Double SND_IMPACT);

}
