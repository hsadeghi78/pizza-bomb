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

describe('ClassType e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/class-types*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('class-type');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load ClassTypes', () => {
    cy.intercept('GET', '/api/class-types*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('class-type');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('ClassType').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details ClassType page', () => {
    cy.intercept('GET', '/api/class-types*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('class-type');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('classType');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create ClassType page', () => {
    cy.intercept('GET', '/api/class-types*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('class-type');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ClassType');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit ClassType page', () => {
    cy.intercept('GET', '/api/class-types*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('class-type');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('ClassType');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of ClassType', () => {
    cy.intercept('GET', '/api/class-types*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('class-type');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ClassType');

    cy.get(`[data-cy="title"]`).type('آذربایجان سودان', { force: true }).invoke('val').should('match', new RegExp('آذربایجان سودان'));

    cy.get(`[data-cy="typeCode"]`).type('11362').should('have.value', '11362');

    cy.get(`[data-cy="description"]`).type('صورتی ای لرستان', { force: true }).invoke('val').should('match', new RegExp('صورتی ای لرستان'));

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/class-types*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('class-type');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of ClassType', () => {
    cy.intercept('GET', '/api/class-types*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/class-types/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('class-type');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('classType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/class-types*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('class-type');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
