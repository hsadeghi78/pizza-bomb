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

describe('MenuItem e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/menu-items*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('menu-item');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load MenuItems', () => {
    cy.intercept('GET', '/api/menu-items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('menu-item');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('MenuItem').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details MenuItem page', () => {
    cy.intercept('GET', '/api/menu-items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('menu-item');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('menuItem');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create MenuItem page', () => {
    cy.intercept('GET', '/api/menu-items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('menu-item');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('MenuItem');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit MenuItem page', () => {
    cy.intercept('GET', '/api/menu-items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('menu-item');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('MenuItem');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of MenuItem', () => {
    cy.intercept('GET', '/api/menu-items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('menu-item');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('MenuItem');

    cy.get(`[data-cy="title"]`).type('اردبیل ماهی', { force: true }).invoke('val').should('match', new RegExp('اردبیل ماهی'));


    cy.get(`[data-cy="expirationDate"]`).type('2021-07-17T22:46').invoke('val').should('equal', '2021-07-17T22:46');


    cy.get(`[data-cy="description"]`).type('متصدی اعتباری درهم', { force: true }).invoke('val').should('match', new RegExp('متصدی اعتباری درهم'));

    cy.setFieldSelectToLastOfEntity('party');

    cy.setFieldSelectToLastOfEntity('food');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/menu-items*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('menu-item');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of MenuItem', () => {
    cy.intercept('GET', '/api/menu-items*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/menu-items/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('menu-item');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('menuItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/menu-items*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('menu-item');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
