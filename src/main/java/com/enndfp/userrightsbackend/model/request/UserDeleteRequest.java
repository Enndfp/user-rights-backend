package com.enndfp.userrightsbackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Enndfp
 */
@Data
public class UserDeleteRequest implements Serializable {

    private static final long serialVersionUID = 1506262083509319831L;

    private Long id;
}
