package com.github.yilmazbahadir.kalah.domain.model.impl;

import com.github.yilmazbahadir.kalah.domain.model.Player;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@EqualsAndHashCode
@Setter
@Getter
@AllArgsConstructor
public class KalahPlayer implements Player {

    private static final long serialVersionUID = 7827181737961530565L;

    private int id;
    private String name;

}
