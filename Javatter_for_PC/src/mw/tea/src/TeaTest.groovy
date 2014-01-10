package mw.tea.src
/**
 * javadoc here.
 * @author Getaji
 */
class TeaTest extends GroovyTestCase {

    void testGetPluginName() {
        assert 1 == 1
    }

    void testGetVersion() {
        Tea tea = new Tea();
        //assert Integer.parseInt(tea.getVersion().split(".")[0]) == 1
    }
}
