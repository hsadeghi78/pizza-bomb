import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Classification e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/classifications*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('classification');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Classifications', () => {
    cy.intercept('GET', '/api/classifications*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('classification');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Classification').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Classification page', () => {
    cy.intercept('GET', '/api/classifications*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('classification');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('classification');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Classification page', () => {
    cy.intercept('GET', '/api/classifications*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('classification');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Classification');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Classification page', () => {
    cy.intercept('GET', '/api/classifications*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('classification');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Classification');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Classification', () => {
    cy.intercept('GET', '/api/classifications*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('classification');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Classification');

    cy.get(`[data-cy="title"]`).type('محدودیت', { force: true }).invoke('val').should('match', new RegExp('محدودیت'));


    cy.get(`[data-cy="classCode"]`).type('10647').should('have.value', '10647');


    cy.get(`[data-cy="description"]`).type('انعطاف', { force: true }).invoke('val').should('match', new RegExp('انعطاف'));

    cy.setFieldSelectToLastOfEntity('classType');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/classifications*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('classification');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Classification', () => {
    cy.intercept('GET', '/api/classifications*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/classifications/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('classification');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('classification').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/classifications*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('classification');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
