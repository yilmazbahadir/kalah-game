package com.github.yilmazbahadir.kalah.domain.model.impl;

import com.github.yilmazbahadir.kalah.domain.model.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@Setter
@Getter
@AllArgsConstructor
public class KalahPlayer implements Player {

    private long id;
    private String name;

}
