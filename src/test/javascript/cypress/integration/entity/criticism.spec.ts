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

describe('Criticism e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/criticisms*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('criticism');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Criticisms', () => {
    cy.intercept('GET', '/api/criticisms*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('criticism');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Criticism').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Criticism page', () => {
    cy.intercept('GET', '/api/criticisms*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('criticism');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('criticism');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Criticism page', () => {
    cy.intercept('GET', '/api/criticisms*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('criticism');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Criticism');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Criticism page', () => {
    cy.intercept('GET', '/api/criticisms*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('criticism');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Criticism');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Criticism', () => {
    cy.intercept('GET', '/api/criticisms*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('criticism');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Criticism');

    cy.get(`[data-cy="fullName"]`).type('درجه سپرده', { force: true }).invoke('val').should('match', new RegExp('درجه سپرده'));

    cy.get(`[data-cy="email"]`).type('_@yahoo.com', { force: true }).invoke('val').should('match', new RegExp('_@yahoo.com'));

    cy.get(`[data-cy="contactNumber"]`)
      .type('ارگونومیک فنلان', { force: true })
      .invoke('val')
      .should('match', new RegExp('ارگونومیک فنلان'));

    cy.get(`[data-cy="description"]`).type('جذاب توانمند', { force: true }).invoke('val').should('match', new RegExp('جذاب توانمند'));

    cy.setFieldSelectToLastOfEntity('party');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/criticisms*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('criticism');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Criticism', () => {
    cy.intercept('GET', '/api/criticisms*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/criticisms/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('criticism');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('criticism').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/criticisms*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('criticism');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
