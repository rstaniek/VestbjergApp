package com.teamSuperior.core.testingUtilities;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Created by rajmu on 17.03.01.
 */
public class Retry implements TestRule {

    private int rc;

    public Retry(int rc) {
        this.rc = rc;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return statement(statement, description);
    }

    private Statement statement(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable throwable = null;
                for (int i = 0; i < rc; i++) {
                    try {
                        base.evaluate();
                        return;
                    } catch (Throwable e) {
                        throwable = e;
                        System.err.println(String.format("Run %d failed!", i + 1));
                    }
                }
                System.out.println(String.format("Giving up after %d failures.", rc));
                throw throwable;
            }
        };
    }
}
