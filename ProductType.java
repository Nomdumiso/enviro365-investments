package com.enviro.assessment.junior.nomdumiso.entity;

/**
 * The kind of investment product held within a portfolio.
 * RETIREMENT_ANNUITY triggers the age > 65 withdrawal rule.
 */
public enum ProductType {
    RETIREMENT_ANNUITY,
    UNIT_TRUST,
    SAVINGS_PLAN
}
