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

describe('Partner e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/partners*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('partner');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Partners', () => {
    cy.intercept('GET', '/api/partners*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('partner');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Partner').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Partner page', () => {
    cy.intercept('GET', '/api/partners*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('partner');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('partner');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Partner page', () => {
    cy.intercept('GET', '/api/partners*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('partner');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Partner');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Partner page', () => {
    cy.intercept('GET', '/api/partners*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('partner');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Partner');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Partner', () => {
    cy.intercept('GET', '/api/partners*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('partner');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Partner');

    cy.get(`[data-cy="title"]`).type('چیپس کردستان', { force: true }).invoke('val').should('match', new RegExp('چیپس کردستان'));

    cy.get(`[data-cy="partnerCode"]`).type('کتان چمران', { force: true }).invoke('val').should('match', new RegExp('کتان چمران'));

    cy.get(`[data-cy="tradeTitle"]`)
      .type('digital نگرش circuit', { force: true })
      .invoke('val')
      .should('match', new RegExp('digital نگرش circuit'));

    cy.get(`[data-cy="economicCode"]`)
      .type('پرداخت آینده کتان', { force: true })
      .invoke('val')
      .should('match', new RegExp('پرداخت آینده کتان'));

    cy.get(`[data-cy="activityDate"]`).type('2021-07-17').should('have.value', '2021-07-17');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/partners*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('partner');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Partner', () => {
    cy.intercept('GET', '/api/partners*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/partners/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('partner');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('partner').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/partners*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('partner');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
