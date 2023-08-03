package dev.muscaw.monitor.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.muscaw.monitor.app.domain.AppService;
import dev.muscaw.monitor.app.domain.NoMatchingAppException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppSelectorImplTest {

  private AppService service1;
  private AppService service2;
  private AppService service3;
  private AppSelectorImpl appSelector;

  private AppService mockAppService(String name) {
    AppService s = mock(AppService.class);
    when(s.getName()).thenReturn(name);
    return s;
  }

  @BeforeEach
  public void setUp() {
    service1 = mockAppService("service1");
    service2 = mockAppService("service2");
    service3 = mockAppService("service3");
    appSelector = new AppSelectorImpl();
    appSelector.setServices(List.of(service1, service2, service3));
  }

  @Test
  public void setServices_rejectsDuplicateServices() {
    var service1Bis = mockAppService("service1");
    var service2Bis = mockAppService("service2");
    RuntimeException ex =
        assertThrows(
            RuntimeException.class,
            () ->
                appSelector.setServices(
                    List.of(service1Bis, service1, service2Bis, service2, service3)));
    assertEquals("Multiple AppService have the same name: service1, service2", ex.getMessage());
  }

  @Test
  public void selectApp_success() {
    assertEquals(service1, appSelector.selectApp("service1"));
    assertEquals(service2, appSelector.selectApp("service2"));
    assertEquals(service3, appSelector.selectApp("service3"));
  }

  @Test
  public void selectApp_notFound() {
    NoMatchingAppException ex =
        assertThrows(NoMatchingAppException.class, () -> appSelector.selectApp("unknown-app"));
    assertEquals("Could not find app unknown-app", ex.getMessage());
  }
}
