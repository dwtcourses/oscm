/**
 * *****************************************************************************
 *
 * <p>Copyright FUJITSU LIMITED 2018
 *
 * <p>Creation Date: 30.08.2016
 *
 * <p>*****************************************************************************
 */
package org.oscm.tenant.bean;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.oscm.dataservice.local.DataService;
import org.oscm.domobjects.Organization;
import org.oscm.domobjects.Tenant;
import org.oscm.internal.types.exception.NonUniqueBusinessKeyException;
import org.oscm.internal.types.exception.ObjectNotFoundException;
import org.oscm.tenant.dao.TenantDao;

public class TenantServiceLocalBeanTest {

  private TenantServiceLocalBean tenantServiceLocalBean;
  private TenantDao tenantDao;
  private DataService dataService;

  @Before
  public void setup() {
    tenantServiceLocalBean = spy(new TenantServiceLocalBean());
    tenantDao = mock(TenantDao.class);
    dataService = mock(DataService.class);
    tenantServiceLocalBean.setTenantDao(tenantDao);
    tenantServiceLocalBean.setDataManager(dataService);
  }

  @Test
  public void testGetAllTenants() {
    // given
    when(tenantDao.getAllTenants()).thenReturn(new ArrayList<Tenant>());

    // when
    tenantServiceLocalBean.getAllTenants();

    // then
    verify(tenantDao, times(1)).getAllTenants();
  }

  @Test
  public void testGetTenantByTenantId() throws ObjectNotFoundException {
    // given
    when(tenantDao.getTenantByTenantId(anyString())).thenReturn(new Tenant());

    // when
    tenantServiceLocalBean.getTenantByTenantId("tenantId");

    // then
    verify(tenantDao, times(1)).getTenantByTenantId("tenantId");
  }

  @Test
  public void testSaveTenant() throws NonUniqueBusinessKeyException {
    // given
    doNothing().when(dataService).persist(any(Tenant.class));

    // when
    tenantServiceLocalBean.saveTenant(new Tenant());

    // then
    verify(dataService, times(1)).persist(any(Tenant.class));
  }

  @Test
  public void testGetTenantByKey() throws ObjectNotFoundException {
    // given
    Tenant t = new Tenant();
    t.setKey(1L);
    when(dataService.getReference(Tenant.class, 1L)).thenReturn(t);

    // when
    Tenant result = tenantServiceLocalBean.getTenantByKey(1L);

    // then
    assertEquals(result.getKey(), t.getKey());
  }

  @Test
  public void testRemovetenant() {
    // given
    doNothing().when(dataService).remove(any(Tenant.class));

    // when
    tenantServiceLocalBean.removeTenant(new Tenant());

    // then
    verify(dataService, times(1)).remove(any(Tenant.class));
  }

  @Test
  public void testGetTenantsByIdPattern() throws ObjectNotFoundException {

    // given
    when(tenantDao.getTenantsByIdPattern("tenantId")).thenReturn(new ArrayList<Tenant>());

    // when
    tenantServiceLocalBean.getTenantsByIdPattern("tenantId");

    // then
    verify(tenantDao, times(1)).getTenantsByIdPattern("tenantId");
  }

  @Test
  public void testDoesOrganizationAssignedToTenantExist() {
    // given
    doReturn(2L).when(tenantDao).doesOrganizationForTenantExist(any(Tenant.class));

    // when
    boolean result = tenantServiceLocalBean.doesOrganizationAssignedToTenantExist(new Tenant());

    // then
    assertTrue(result);
  }

  @Test
  public void testDoesMarketplaceAssignedToTenantExist() {
    // given
    doReturn(2L).when(tenantDao).doesMarketplaceAssignedToTenantExist(any(Tenant.class));

    // when
    boolean result = tenantServiceLocalBean.doesMarketplaceAssignedToTenantExist(new Tenant());

    // then
    assertTrue(result);
  }

  @Test
  public void testDoesOrganizationAssignedToTenantExist_notExists() {
    // given
    doReturn(new Long(0L)).when(tenantDao).doesOrganizationForTenantExist(any(Tenant.class));

    // when
    boolean result = tenantServiceLocalBean.doesOrganizationAssignedToTenantExist(new Tenant());

    // then
    assertFalse(result);
  }

  @Test
  public void testDoesMarketplaceAssignedToTenantExist_notExists() {
    // given
    doReturn(new Long(0L)).when(tenantDao).doesMarketplaceAssignedToTenantExist(any(Tenant.class));

    // when
    boolean result = tenantServiceLocalBean.doesMarketplaceAssignedToTenantExist(new Tenant());

    // then
    assertFalse(result);
  }

  @Test
  public void doesOrgNameExistInTenantTrue() {
    final List<Organization> list = new ArrayList<>();
    list.add(mock(Organization.class));
    when(tenantDao.getOrgNameInTenant("org", "tenant")).thenReturn(list);

    boolean result = tenantServiceLocalBean.doesOrgNameExistInTenant("org", "tenant");

    assertTrue(result);
  }

  @Test
  public void doesOrgNameExistInTenantFalseListEmpty() {
    final List<Organization> list = new ArrayList<>();
    when(tenantDao.getOrgNameInTenant("org", "tenant")).thenReturn(list);

    boolean result = tenantServiceLocalBean.doesOrgNameExistInTenant("org", "tenant");

    assertFalse(result);
  }

  @Test
  public void doesOrgNameExistInTenantFalseListNull() {
    when(tenantDao.getOrgNameInTenant("org", "tenant")).thenReturn(null);

    boolean result = tenantServiceLocalBean.doesOrgNameExistInTenant("org", "tenant");

    assertFalse(result);
  }
}
