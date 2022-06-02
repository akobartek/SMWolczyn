package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.DialogBreviarySelectOfficeBinding
import pl.kapucyni.wolczyn.app.databinding.FragmentBreviaryBinding
import pl.kapucyni.wolczyn.app.utils.showNoInternetDialogWithTryAgain
import pl.kapucyni.wolczyn.app.utils.tryToRunFunctionOnInternet
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import pl.kapucyni.wolczyn.app.viewmodels.BreviaryViewModel

class BreviaryFragment : BindingFragment<FragmentBreviaryBinding>() {

    private val mViewModel: BreviaryViewModel by activityViewModels()
    private val mLoadingDialog: AlertDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.dialog_loading)
            .setOnCancelListener { requireActivity().onBackPressed() }
            .create()
    }

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentBreviaryBinding.inflate(inflater, container, false)

    override fun setup(savedInstanceState: Bundle?) {
        when {
            savedInstanceState != null -> binding.breviaryText.restoreState(savedInstanceState)
            else -> requireActivity().tryToRunFunctionOnInternet { checkIfThereAreMultipleOffices() }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.breviaryText.saveState(outState)
    }

    private fun checkIfThereAreMultipleOffices() {
        mLoadingDialog.show()
        mViewModel.checkIfThereAreMultipleOffices(
            { offices /* offices are pairs of <code, name> */ ->
                if (offices != null) showSelectOfficeDialog(offices)
                else loadBreviary()
            }, {
                if (mLoadingDialog.isShowing) mLoadingDialog.hide()
                activity?.showNoInternetDialogWithTryAgain { checkIfThereAreMultipleOffices() }
            })
    }

    private fun showSelectOfficeDialog(offices: List<Pair<String, String>>) {
        if (mLoadingDialog.isShowing) mLoadingDialog.hide()

        val dialogBinding = DialogBreviarySelectOfficeBinding.inflate(layoutInflater)
        dialogBinding.selectOfficeList.apply {
            adapter = ArrayAdapter(
                requireContext(),
                R.layout.dialog_item_listview_select,
                offices.map { it.second })
            setItemChecked(0, true)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                setItemChecked(position, true)
            }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .setPositiveButton(R.string.load) { dialog, _ ->
                dialog.dismiss()
                mLoadingDialog.show()
                loadBreviary(offices[dialogBinding.selectOfficeList.checkedItemPosition].first)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                (requireActivity() as MainActivity).goToSelectedFragment(R.id.nav_breviary)
            }
            .create()
            .show()
    }

    private fun loadBreviary(office: String = "") {
        mViewModel.loadBreviaryHtml(
            office,
            requireArguments().getInt("position"),
            { textToShow ->
                if (mLoadingDialog.isShowing) mLoadingDialog.hide()
                binding.breviaryText.apply {
                    loadDataWithBaseURL(
                        null, textToShow, "text/html", null, null
                    )
                    visibility = View.VISIBLE
                    scrollTo(0, 0)
                }
            }, {
                if (mLoadingDialog.isShowing) mLoadingDialog.hide()
                activity?.showNoInternetDialogWithTryAgain { loadBreviary(office) }
            }
        )
    }

    companion object {
        fun newInstance(position: Int): BreviaryFragment {
            return BreviaryFragment().apply {
                arguments = Bundle().apply { putInt("position", position) }
            }
        }
    }
}
