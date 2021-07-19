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

describe('FactorItem e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/factor-items*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('factor-item');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load FactorItems', () => {
    cy.intercept('GET', '/api/factor-items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-item');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('FactorItem').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details FactorItem page', () => {
    cy.intercept('GET', '/api/factor-items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-item');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('factorItem');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create FactorItem page', () => {
    cy.intercept('GET', '/api/factor-items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-item');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('FactorItem');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit FactorItem page', () => {
    cy.intercept('GET', '/api/factor-items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-item');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('FactorItem');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of FactorItem', () => {
    cy.intercept('GET', '/api/factor-items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-item');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('FactorItem');

    cy.get(`[data-cy="rowNum"]`).type('47666').should('have.value', '47666');


    cy.get(`[data-cy="title"]`).type('ها سازی بتنی', { force: true }).invoke('val').should('match', new RegExp('ها سازی بتنی'));


    cy.get(`[data-cy="count"]`).type('14614').should('have.value', '14614');


    cy.get(`[data-cy="discount"]`).type('16046').should('have.value', '16046');


    cy.get(`[data-cy="tax"]`).type('26247').should('have.value', '26247');


    cy.get(`[data-cy="description"]`).type('مدیر اندرزگو', { force: true }).invoke('val').should('match', new RegExp('مدیر اندرزگو'));

    cy.setFieldSelectToLastOfEntity('food');

    cy.setFieldSelectToLastOfEntity('factor');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/factor-items*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-item');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of FactorItem', () => {
    cy.intercept('GET', '/api/factor-items*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/factor-items/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor-item');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('factorItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/factor-items*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('factor-item');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
