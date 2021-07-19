import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'party',
        data: { pageTitle: 'bombApp.party.home.title' },
        loadChildren: () => import('./party/party.module').then(m => m.PartyModule),
      },
      {
        path: 'branch',
        data: { pageTitle: 'bombApp.branch.home.title' },
        loadChildren: () => import('./branch/branch.module').then(m => m.BranchModule),
      },
      {
        path: 'criticism',
        data: { pageTitle: 'bombApp.criticism.home.title' },
        loadChildren: () => import('./criticism/criticism.module').then(m => m.CriticismModule),
      },
      {
        path: 'address',
        data: { pageTitle: 'bombApp.address.home.title' },
        loadChildren: () => import('./address/address.module').then(m => m.AddressModule),
      },
      {
        path: 'contact',
        data: { pageTitle: 'bombApp.contact.home.title' },
        loadChildren: () => import('./contact/contact.module').then(m => m.ContactModule),
      },
      {
        path: 'partner',
        data: { pageTitle: 'bombApp.partner.home.title' },
        loadChildren: () => import('./partner/partner.module').then(m => m.PartnerModule),
      },
      {
        path: 'person',
        data: { pageTitle: 'bombApp.person.home.title' },
        loadChildren: () => import('./person/person.module').then(m => m.PersonModule),
      },
      {
        path: 'party-information',
        data: { pageTitle: 'bombApp.partyInformation.home.title' },
        loadChildren: () => import('./party-information/party-information.module').then(m => m.PartyInformationModule),
      },
      {
        path: 'comment',
        data: { pageTitle: 'bombApp.comment.home.title' },
        loadChildren: () => import('./comment/comment.module').then(m => m.CommentModule),
      },
      {
        path: 'class-type',
        data: { pageTitle: 'bombApp.classType.home.title' },
        loadChildren: () => import('./class-type/class-type.module').then(m => m.ClassTypeModule),
      },
      {
        path: 'classification',
        data: { pageTitle: 'bombApp.classification.home.title' },
        loadChildren: () => import('./classification/classification.module').then(m => m.ClassificationModule),
      },
      {
        path: 'food-type',
        data: { pageTitle: 'bombApp.foodType.home.title' },
        loadChildren: () => import('./food-type/food-type.module').then(m => m.FoodTypeModule),
      },
      {
        path: 'food',
        data: { pageTitle: 'bombApp.food.home.title' },
        loadChildren: () => import('./food/food.module').then(m => m.FoodModule),
      },
      {
        path: 'factor',
        data: { pageTitle: 'bombApp.factor.home.title' },
        loadChildren: () => import('./factor/factor.module').then(m => m.FactorModule),
      },
      {
        path: 'factor-item',
        data: { pageTitle: 'bombApp.factorItem.home.title' },
        loadChildren: () => import('./factor-item/factor-item.module').then(m => m.FactorItemModule),
      },
      {
        path: 'menu-item',
        data: { pageTitle: 'bombApp.menuItem.home.title' },
        loadChildren: () => import('./menu-item/menu-item.module').then(m => m.MenuItemModule),
      },
      {
        path: 'price-history',
        data: { pageTitle: 'bombApp.priceHistory.home.title' },
        loadChildren: () => import('./price-history/price-history.module').then(m => m.PriceHistoryModule),
      },
      {
        path: 'factor-status-history',
        data: { pageTitle: 'bombApp.factorStatusHistory.home.title' },
        loadChildren: () => import('./factor-status-history/factor-status-history.module').then(m => m.FactorStatusHistoryModule),
      },
      {
        path: 'consume-material',
        data: { pageTitle: 'bombApp.consumeMaterial.home.title' },
        loadChildren: () => import('./consume-material/consume-material.module').then(m => m.ConsumeMaterialModule),
      },
      {
        path: 'file-document',
        data: { pageTitle: 'bombApp.fileDocument.home.title' },
        loadChildren: () => import('./file-document/file-document.module').then(m => m.FileDocumentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
