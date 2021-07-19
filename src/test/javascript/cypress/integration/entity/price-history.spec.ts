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

describe('PriceHistory e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/price-histories*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('price-history');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load PriceHistories', () => {
    cy.intercept('GET', '/api/price-histories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('price-history');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('PriceHistory').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details PriceHistory page', () => {
    cy.intercept('GET', '/api/price-histories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('price-history');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('priceHistory');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create PriceHistory page', () => {
    cy.intercept('GET', '/api/price-histories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('price-history');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('PriceHistory');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit PriceHistory page', () => {
    cy.intercept('GET', '/api/price-histories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('price-history');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('PriceHistory');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of PriceHistory', () => {
    cy.intercept('GET', '/api/price-histories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('price-history');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('PriceHistory');

    cy.get(`[data-cy="foodId"]`).type('80165').should('have.value', '80165');

    cy.get(`[data-cy="materialId"]`).type('88761').should('have.value', '88761');

    cy.get(`[data-cy="price"]`).type('92119').should('have.value', '92119');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/price-histories*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('price-history');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of PriceHistory', () => {
    cy.intercept('GET', '/api/price-histories*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/price-histories/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('price-history');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('priceHistory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/price-histories*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('price-history');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
