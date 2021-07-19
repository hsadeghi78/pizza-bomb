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

describe('ConsumeMaterial e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/consume-materials*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('consume-material');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load ConsumeMaterials', () => {
    cy.intercept('GET', '/api/consume-materials*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('consume-material');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('ConsumeMaterial').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details ConsumeMaterial page', () => {
    cy.intercept('GET', '/api/consume-materials*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('consume-material');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('consumeMaterial');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create ConsumeMaterial page', () => {
    cy.intercept('GET', '/api/consume-materials*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('consume-material');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ConsumeMaterial');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit ConsumeMaterial page', () => {
    cy.intercept('GET', '/api/consume-materials*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('consume-material');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('ConsumeMaterial');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of ConsumeMaterial', () => {
    cy.intercept('GET', '/api/consume-materials*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('consume-material');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('ConsumeMaterial');

    cy.get(`[data-cy="title"]`).type('و', { force: true }).invoke('val').should('match', new RegExp('و'));


    cy.get(`[data-cy="type"]`).type('تجربیات رئیس', { force: true }).invoke('val').should('match', new RegExp('تجربیات رئیس'));


    cy.get(`[data-cy="amount"]`).type('66388').should('have.value', '66388');


    cy.get(`[data-cy="amountUnitClassId"]`).type('45060').should('have.value', '45060');

    cy.setFieldSelectToLastOfEntity('food');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/consume-materials*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('consume-material');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of ConsumeMaterial', () => {
    cy.intercept('GET', '/api/consume-materials*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/consume-materials/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('consume-material');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('consumeMaterial').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/consume-materials*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('consume-material');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
