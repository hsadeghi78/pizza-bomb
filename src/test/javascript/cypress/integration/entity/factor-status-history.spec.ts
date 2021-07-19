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

describe('FactorStatusHistory e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/factor-status-histories*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('factor-status-history');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load FactorStatusHistories', () => {
    cy.intercept('GET', '/api/factor-status-histories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-status-history');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('FactorStatusHistory').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details FactorStatusHistory page', () => {
    cy.intercept('GET', '/api/factor-status-histories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-status-history');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('factorStatusHistory');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create FactorStatusHistory page', () => {
    cy.intercept('GET', '/api/factor-status-histories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-status-history');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('FactorStatusHistory');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit FactorStatusHistory page', () => {
    cy.intercept('GET', '/api/factor-status-histories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-status-history');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('FactorStatusHistory');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of FactorStatusHistory', () => {
    cy.intercept('GET', '/api/factor-status-histories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-status-history');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('FactorStatusHistory');

    cy.get(`[data-cy="factorId"]`).type('91945').should('have.value', '91945');

    cy.get(`[data-cy="status"]`).select('INITIATE');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/factor-status-histories*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-status-history');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of FactorStatusHistory', () => {
    cy.intercept('GET', '/api/factor-status-histories*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/factor-status-histories/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-status-history');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('factorStatusHistory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/factor-status-histories*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('factor-status-history');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
